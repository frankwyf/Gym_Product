package com.gymmaster.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmaster.common.BackMsg;
import com.gymmaster.common.CurrentUserResolver;
import com.gymmaster.entity.Account;
import com.gymmaster.entity.Bill;
import com.gymmaster.entity.Customer;
import com.gymmaster.entity.Goods;
import com.gymmaster.entity.Reservation;
import com.gymmaster.entity.Venue;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.qr.QrCodeUtils;
import com.gymmaster.service.AccountService;
import com.gymmaster.service.BillService;
import com.gymmaster.service.FacilityService;
import com.gymmaster.service.ReservationService;
import com.gymmaster.service.VenueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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

    @GetMapping("/page/period")
    public BackMsg<Map<String, BigDecimal>> pagePeriod(Timestamp start, Timestamp endTime) {
        if (start == null) {
            Calendar calendar = Calendar.getInstance();
            calendar.set(2002, Calendar.JUNE, 27);
            start = new Timestamp(calendar.getTimeInMillis());
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

    @GetMapping("/showall")
    public BackMsg<List<Bill>> showall(HttpServletRequest request) {
        Customer customer = currentUserResolver.getLoginUser(request).getCustomer();
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<Bill>()
                .eq(Bill::getUid, customer.getUid())
                .orderByDesc(Bill::getBid);
        return BackMsg.success(billService.list(queryWrapper));
    }

    @PostMapping("/pay")
    public BackMsg<String> pay(
            @RequestBody Map<String, Object> goodlist,
            int aid,
            double total,
            HttpServletRequest request) throws ParseException {
        Customer customer = currentUserResolver.getLoginUser(request).getCustomer();
        int userId = customer.getUid();
        List<Goods> goods = parseGoods(goodlist);

        for (Goods good : goods) {
            Reservation reservation = buildReservation(good, userId);
            if (!"0".equals(good.getPeriod())) {
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
            try {
                QrCodeUtils.encode(reservation.toString(), qrLogoPath, destPath, true);
            } catch (Exception e) {
                log.error("Failed to generate reservation QR for rid={}", reservation.getRid(), e);
                throw new BusinessException("Failed to generate reservation QR code.");
            }
            good.setReservation(reservation);
        }

        Account account = accountService.getOne(
                new LambdaQueryWrapper<Account>().eq(Account::getAid, aid));
        if (account == null) {
            throw new BusinessException("Account does not exist.");
        }

        double discountRate = discountRate(customer.getMembership());
        for (Goods good : goods) {
            Reservation reservation = good.getReservation();
            BigDecimal charge = BigDecimal.valueOf(good.getPrice())
                    .multiply(BigDecimal.valueOf(discountRate))
                    .multiply(BigDecimal.valueOf(good.getAmount()));
            BigDecimal balance = account.getBalance().subtract(charge);
            if (balance.compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("Account balance not enough.");
            }
            account.setBalance(balance);

            Venue venue = venueService.getById(reservation.getVenue());
            com.gymmaster.entity.Facility facility = facilityService.getById(reservation.getFacility());

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
            reservationService.update(
                    reservation,
                    new LambdaQueryWrapper<Reservation>().eq(Reservation::getRid, reservation.getRid()));
        }

        accountService.update(account, new LambdaQueryWrapper<Account>().eq(Account::getAid, aid));
        return BackMsg.success("success");
    }

    @SuppressWarnings("unchecked")
    private static List<Goods> parseGoods(Map<String, Object> goodlist) {
        List<Map<String, Object>> goodsMaps = (List<Map<String, Object>>) goodlist.get("goodlist");
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

    private static Reservation buildReservation(Goods good, int userId) throws ParseException {
        Reservation reservation = new Reservation();
        reservation.setRuid(userId);
        reservation.setAmount(good.getAmount());
        reservation.setPeriod(good.getPeriod());
        reservation.setFacility(good.getFacility());
        reservation.setVenue(good.getVenue());
        reservation.setStatus("unpaid");
        reservation.setPayment("account");

        String rawDate = good.getDate();
        String normalized = rawDate.length() > 5 ? rawDate.substring(0, 10) : "2023-" + rawDate;
        java.util.Date parsed = new SimpleDateFormat("yyyy-MM-dd").parse(normalized);
        reservation.setRdate(new Date(parsed.getTime()));
        return reservation;
    }

    private static void validatePeriodCapacity(Reservation reservation, int[] remaining) {
        for (String part : reservation.getPeriod().split(",")) {
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

    static double discountRate(String membershipType) {
        if (membershipType == null) {
            return 1.0;
        }
        return switch (membershipType) {
            case "copper member" -> 0.8;
            case "silver member" -> 0.6;
            case "gold member" -> 0.3;
            default -> 1.0;
        };
    }
}