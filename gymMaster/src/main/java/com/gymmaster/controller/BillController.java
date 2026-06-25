package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmaster.common.BackMsg;
import com.gymmaster.common.CurrentUserResolver;
import com.gymmaster.entity.*;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.qr.QrCodeUtils;
import com.gymmaster.service.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/bill")
@RequiredArgsConstructor
public class BillController {

    private final BillService billService;
    private final FacilityService facilityService;
    private final VenueService venueService;
    private final AccountService accountService;
    private final ReservationService reservationService;
    private final CurrentUserResolver currentUserResolver;

    @Value("${qr.logo.path:src/main/resources/static/logo/logo.png}")
    private String qrLogoPath;

    @Value("${qr.reservation.dir:src/main/resources/static/reservationQR/reservation/}")
    private String qrReservationDir;

    /**
     * Revenue statistics grouped by facility name within the given time range.
     * If no start/end provided, defaults to 2002-06-27 → now.
     */
    @GetMapping("/page/period")
    public BackMsg<Map<String, BigDecimal>> pagePeriod(Timestamp start, Timestamp endTime) {
        if (start == null) {
            Calendar c = Calendar.getInstance();
            c.set(2002, Calendar.JUNE, 27);
            start = new Timestamp(c.getTimeInMillis());
        }
        if (endTime == null) {
            endTime = new Timestamp(System.currentTimeMillis());
        }
        if (start.after(endTime)) {
            return BackMsg.error("wrong date entered!");
        }

        List<Bill> orders = billService.list(null);
        Timestamp finalStart = start;
        Timestamp finalEnd = endTime;
        orders.removeIf(bill -> bill.getBdate().before(finalStart) || bill.getBdate().after(finalEnd));

        Map<String, BigDecimal> statistic = new HashMap<>();
        for (Bill bill : orders) {
            statistic.merge(bill.getFname(), bill.getFigure(), BigDecimal::add);
        }
        return BackMsg.success(statistic);
    }

    @GetMapping("/page/facility")
    public BackMsg<Page<Bill>> page(int page, int pageSize, String name) {
        Page<Bill> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<Bill>()
                .like(StringUtils.isNotEmpty(name), Bill::getFname, name)
                .orderByDesc(Bill::getBid);
        billService.page(pageInfo, queryWrapper);
        return BackMsg.success(pageInfo);
    }

    /** Show all bills for the authenticated customer. */
    @GetMapping("/showall")
    public BackMsg<List<Bill>> showall(HttpServletRequest request) {
        Customer customer = currentUserResolver.getLoginUser(request).getCustomer();
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<Bill>()
                .eq(Bill::getUid, customer.getUid())
                .orderByDesc(Bill::getBid);
        return BackMsg.success(billService.list(queryWrapper));
    }

    /**
     * Payment endpoint: validates capacity, creates reservations + bills, and deducts account balance.
     *
     * @param goodlist parsed cart items
     * @param aid      account ID to charge
     * @param total    expected total (informational; actual total is recomputed server-side)
     */
    @PostMapping("/pay")
    public BackMsg<String> pay(
            @RequestBody Map<String, Object> goodlist,
            int aid,
            double total,
            HttpServletRequest request) throws ParseException {

        Customer customer = currentUserResolver.getLoginUser(request).getCustomer();
        int userId = customer.getUid();

        List<Goods> goods = parseGoods(goodlist);

        // ── Phase 1: validate capacity + create reservations ─────────────────
        for (Goods good : goods) {
            Reservation reservation = buildReservation(good, userId);

            if (!good.getPeriod().equals("0")) {
                Venue venue = venueService.getOne(new LambdaQueryWrapper<Venue>()
                        .eq(Venue::getVid, reservation.getVenue())
                        .eq(Venue::getFid, reservation.getFacility()));
                if (venue == null) {
                    throw new BusinessException("Venue not found for reservation.");
                }
                int[] remaining = venueService.remainingCapacity(venue, reservation.getRdate());
                validatePeriodCapacity(reservation, remaining);
            }

            reservationService.save(reservation);
            String destPath = qrReservationDir + reservation.getRid() + ".jpg";
            QrCodeUtils.encode(reservation.toString(), qrLogoPath, destPath, true);
            good.setReservation(reservation);
        }

        // ── Phase 2: charge account + create bills ───────────────────────────
        Account account = accountService.getOne(
                new LambdaQueryWrapper<Account>().eq(Account::getAid, aid));
        if (account == null) {
            throw new BusinessException("Account does not exist.");
        }

        String membership = customer.getMembership();
        double discountRate = discountRate(membership);

        for (Goods good : goods) {
            Reservation reservation = good.getReservation();
            BigDecimal tot = BigDecimal.valueOf(good.getPrice())
                    .multiply(BigDecimal.valueOf(discountRate))
                    .multiply(BigDecimal.valueOf(good.getAmount()));
            BigDecimal leave = account.getBalance().subtract(tot);
            if (leave.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Account balance not enough.");
            }
            account.setBalance(leave);

            Venue venue = venueService.getById(reservation.getVenue());
            com.gymmaster.entity.Facility facility =
                    facilityService.getById(reservation.getFacility());

            Bill bill = new Bill();
            bill.setFigure(BigDecimal.valueOf(good.getPrice()));
            bill.setUid(userId);
            bill.setBdate(new Timestamp(System.currentTimeMillis()));
            bill.setVname(venue != null ? venue.getVname() : "");
            bill.setFname(facility != null ? facility.getFname() : "");
            bill.setBrid(reservation.getRid());
            bill.setOperator("system");
            billService.save(bill);

            reservation.setStatus("valid");
            reservation.setRuid(userId);
            reservationService.update(reservation,
                    new LambdaQueryWrapper<Reservation>().eq(Reservation::getRid, reservation.getRid()));
        }
        accountService.update(account,
                new LambdaQueryWrapper<Account>().eq(Account::getAid, aid));

        return BackMsg.success("success");
    }

    // ─────────────────────────────────────────────────────────────────────────
    // Private helpers
    // ─────────────────────────────────────────────────────────────────────────

    /** Parse the raw {@code goodlist} request body into typed {@link Goods} objects. */
    @SuppressWarnings("unchecked")
    private static List<Goods> parseGoods(Map<String, Object> goodlist) {
        List<Map<String, Object>> goodsMaps =
                (List<Map<String, Object>>) goodlist.get("goodlist");
        List<Goods> result = new ArrayList<>();
        for (Map<String, Object> item : goodsMaps) {
            Goods good = new Goods();
            good.setPeriod(item.get("period").toString());
            good.setFacility(Integer.parseInt(item.get("facility").toString()));
            good.setVenue(Integer.parseInt(item.get("venue").toString()));
            good.setAmount(Integer.parseInt(item.get("amount").toString()));
            good.setPrice(Integer.parseInt(item.get("price").toString()));
            good.setActive((boolean) item.get("active"));
            good.setDate((String) item.get("date"));
            good.setPic((String) item.get("pic"));
            good.setName((String) item.get("name"));
            good.setType((String) item.get("type"));
            result.add(good);
        }
        return result;
    }

    /** Build a {@link Reservation} with status {@code unpaid} from a cart item. */
    private static Reservation buildReservation(Goods good, int userId) throws ParseException {
        Reservation res = new Reservation();
        res.setRuid(userId);
        res.setAmount(good.getAmount());
        res.setPeriod(good.getPeriod());
        res.setFacility(good.getFacility());
        res.setVenue(good.getVenue());
        res.setStatus("unpaid");
        res.setPayment("account");

        String rawDate = good.getDate();
        String normalized = rawDate.length() > 5 ? rawDate.substring(0, 10) : "2023-" + rawDate;
        java.util.Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(normalized);
        res.setRdate(new Date(parsed.getTime()));
        return res;
    }

    /**
     * Validates that the requested periods have enough remaining capacity.
     *
     * @param reservation reservation containing the requested periods + amount
     * @param remaining   remaining capacity per period (index = period - 1)
     * @throws BusinessException if any period is out of range or over capacity
     */
    private static void validatePeriodCapacity(Reservation reservation, int[] remaining) {
        String[] parts = reservation.getPeriod().split(",");
        for (String part : parts) {
            int period;
            try {
                period = Integer.parseInt(part.trim());
            } catch (NumberFormatException e) {
                throw new BusinessException("Invalid period value: " + part);
            }
            if (period < 1 || period > remaining.length) {
                throw new BusinessException("Period out of range: " + period);
            }
            if (remaining[period - 1] < reservation.getAmount()) {
                throw new BusinessException("Period " + period + " has less than "
                        + reservation.getAmount() + " remaining capacity.");
            }
        }
    }

    /**
     * Maps membership type to a discount rate.
     * Moved to a static method so the {@code /pay} endpoint can reuse it without
     * creating a separate bean; business rules live in one place.
     */
    static double discountRate(String membershipType) {
        if (membershipType == null) return 1.0;
        return switch (membershipType) {
            case "copper member" -> 0.8;
            case "silver member" -> 0.6;
            case "gold member"   -> 0.3;
            default              -> 1.0;
        };
    }
}
