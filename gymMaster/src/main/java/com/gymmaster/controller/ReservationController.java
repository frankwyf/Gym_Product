package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmaster.common.BackMsg;
import com.gymmaster.common.CurrentUserResolver;
import com.gymmaster.entity.Customer;
import com.gymmaster.entity.Reservation;
import com.gymmaster.entity.Venue;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.qr.QrCodeUtils;
import com.gymmaster.service.CustomerService;
import com.gymmaster.service.ReservationService;
import com.gymmaster.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.util.List;

/**
 * Reservation management — customer reservation lifecycle and admin overrides.
 *
 * <p>JWT user extraction is centralised via {@link CurrentUserResolver};
 * capacity calculation is delegated to {@link com.gymmaster.service.impl.VenueServiceImpl}.
 */
@Slf4j
@RestController
@RequestMapping("/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;
    private final CustomerService customerService;
    private final VenueService venueService;
    private final CurrentUserResolver currentUserResolver;

    @Value("${qr.logo.path:src/main/resources/static/logo/logo.png}")
    private String qrLogoPath;

    @Value("${qr.reservation.dir:src/main/resources/static/reservationQR/}")
    private String qrReservationDir;

    // ─── Admin queries ────────────────────────────────────────────────────────

    @GetMapping("/page/username")
    public BackMsg<Page<Reservation>> pageUsername(int page, int pageSize, String name) {
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Customer> cqw = new LambdaQueryWrapper<Customer>()
                .like(StringUtils.isNotEmpty(name), Customer::getUsername, name);
        Customer customer = customerService.getOne(cqw);
        if (customer == null) {
            return BackMsg.success(pageInfo);   // no match → empty page
        }
        LambdaQueryWrapper<Reservation> rqw = new LambdaQueryWrapper<Reservation>()
                .eq(Reservation::getRuid, customer.getUid())
                .orderByDesc(Reservation::getRdate);
        reservationService.page(pageInfo, rqw);
        return BackMsg.success(pageInfo);
    }

    @GetMapping("/page/date")
    public BackMsg<Page<Reservation>> pageDate(int page, int pageSize, Date date) {
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Reservation> qw = new LambdaQueryWrapper<Reservation>()
                .like(Reservation::getRdate, date)
                .orderByDesc(Reservation::getRdate);   // was Ruid — wrong sort key
        reservationService.page(pageInfo, qw);
        return BackMsg.success(pageInfo);
    }

    @GetMapping("/page/id")
    public BackMsg<Page<Reservation>> pageId(int page, int pageSize, int id) {
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        reservationService.page(pageInfo,
                new LambdaQueryWrapper<Reservation>().eq(Reservation::getRuid, id));
        return BackMsg.success(pageInfo);
    }

    @GetMapping("/page")
    public BackMsg<Page<Reservation>> page(int page, int pageSize) {
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        reservationService.page(pageInfo,
                new LambdaQueryWrapper<Reservation>().orderByAsc(Reservation::getRid));
        return BackMsg.success(pageInfo);
    }

    // ─── Admin: block/unblock a time slot ────────────────────────────────────

    @PutMapping("/ban")
    public BackMsg<String> ban(@RequestBody Reservation reservation, int x) {
        if (x == 0) {
            // Block: compute remaining capacity for this slot and create a
            // blocking reservation (ruid=0) that consumes it all.
            Venue venue = venueService.getOne(new LambdaQueryWrapper<Venue>()
                    .eq(Venue::getVid, reservation.getVenue())
                    .eq(Venue::getFid, reservation.getFacility()));
            if (venue == null) {
                throw new BusinessException("Venue not found.");
            }
            int[] remaining = venueService.remainingCapacity(venue, reservation.getRdate());
            int periodIdx;
            try {
                periodIdx = Integer.parseInt(reservation.getPeriod());
            } catch (NumberFormatException e) {
                throw new BusinessException("Invalid period: " + reservation.getPeriod());
            }
            if (periodIdx < 1 || periodIdx > remaining.length) {
                throw new BusinessException("Period out of range: " + periodIdx);
            }
            reservation.setAmount(remaining[periodIdx - 1]);
            reservation.setRuid(0);
            reservationService.save(reservation);
        } else {
            // Unblock: mark the blocking reservation as 'unable'
            LambdaQueryWrapper<Reservation> qw = new LambdaQueryWrapper<Reservation>()
                    .eq(Reservation::getRuid, 0)
                    .eq(Reservation::getPeriod, reservation.getPeriod())
                    .eq(Reservation::getVenue, reservation.getVenue())
                    .eq(Reservation::getFacility, reservation.getFacility())
                    .eq(Reservation::getRdate, reservation.getRdate());
            Reservation blocking = reservationService.getOne(qw);
            if (blocking == null) {
                throw new BusinessException("No blocking reservation found to unblock.");
            }
            blocking.setStatus("unable");
            reservationService.update(blocking, qw);
        }
        return BackMsg.success("success");
    }

    // ─── Look-ups ─────────────────────────────────────────────────────────────

    @GetMapping("/findId")
    public BackMsg<Reservation> findById(int id) {
        return BackMsg.success(reservationService.getOne(
                new LambdaQueryWrapper<Reservation>().eq(Reservation::getRid, id)));
    }

    @GetMapping("/findVid")
    public BackMsg<List<Reservation>> findByVenueId(int id) {
        return BackMsg.success(reservationService.list(
                new LambdaQueryWrapper<Reservation>().eq(Reservation::getVenue, id)));
    }

    @GetMapping("/findvname")
    public BackMsg<List<Reservation>> findByVenueName(String name1) {
        Venue venue = venueService.getOne(
                new LambdaQueryWrapper<Venue>().eq(Venue::getVname, name1));
        if (venue == null) {
            throw new BusinessException("Venue not found: " + name1);
        }
        return BackMsg.success(reservationService.list(
                new LambdaQueryWrapper<Reservation>().eq(Reservation::getVenue, venue.getVid())));
    }

    // ─── Customer: create reservation ────────────────────────────────────────

    @PostMapping("/add")
    public BackMsg<String> add(@RequestBody Reservation reservation, HttpServletRequest request)
            throws Exception {
        int userId = currentUserResolver.getUserId(request);

        Venue venue = venueService.getOne(new LambdaQueryWrapper<Venue>()
                .eq(Venue::getVid, reservation.getVenue())
                .eq(Venue::getFid, reservation.getFacility()));
        if (venue == null) {
            throw new BusinessException("Venue not found.");
        }

        int[] remaining = venueService.remainingCapacity(venue, reservation.getRdate());
        validatePeriods(reservation, remaining);

        reservation.setStatus("unpaid");
        reservation.setRuid(userId);
        reservationService.save(reservation);

        String destPath = qrReservationDir + reservation.getRid() + ".jpg";
        QrCodeUtils.encode(reservation.toString(), qrLogoPath, destPath, true);

        return BackMsg.success("reservation added successfully!");
    }

    // ─── Customer: list their own reservations ────────────────────────────────

    @GetMapping("/getUnpaid")
    public BackMsg<List<Reservation>> getUnpaid(HttpServletRequest request) {
        int uid = currentUserResolver.getUserId(request);
        return BackMsg.success(reservationService.list(
                new LambdaQueryWrapper<Reservation>()
                        .eq(Reservation::getRuid, uid)
                        .eq(Reservation::getStatus, "unpaid")));
    }

    @GetMapping("/getPaid")
    public BackMsg<List<Reservation>> getPaid(HttpServletRequest request) {
        int uid = currentUserResolver.getUserId(request);
        return BackMsg.success(reservationService.list(
                new LambdaQueryWrapper<Reservation>()
                        .eq(Reservation::getRuid, uid)
                        .eq(Reservation::getStatus, "valid")));
    }

    // ─── General update ──────────────────────────────────────────────────────

    @PutMapping("/update")
    public BackMsg<String> edit(@RequestBody Reservation reservation) {
        reservationService.update(reservation,
                new LambdaQueryWrapper<Reservation>().eq(Reservation::getRid, reservation.getRid()));
        return BackMsg.success("updated successfully!");
    }

    // ─── Private helpers ─────────────────────────────────────────────────────

    /**
     * Validates all requested periods against the available capacity array.
     *
     * @param reservation target reservation (period + amount must be set)
     * @param remaining   per-period remaining capacity (index = period - 1)
     * @throws BusinessException on invalid period or insufficient capacity
     */
    private static void validatePeriods(Reservation reservation, int[] remaining) {
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
}
