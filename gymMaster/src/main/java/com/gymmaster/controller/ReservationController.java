package com.gymmaster.controller;

import java.sql.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @GetMapping("/page/username")
    public BackMsg<Page<Reservation>> pageUsername(int page, int pageSize, String name) {
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Customer> customerQuery = new LambdaQueryWrapper<Customer>()
                .like(StringUtils.isNotEmpty(name), Customer::getUsername, name);
        Customer customer = customerService.getOne(customerQuery);
        if (customer == null) {
            return BackMsg.success(pageInfo);
        }

        LambdaQueryWrapper<Reservation> reservationQuery = new LambdaQueryWrapper<Reservation>()
                .eq(Reservation::getRuid, customer.getUid())
                .orderByDesc(Reservation::getRdate);
        reservationService.page(pageInfo, reservationQuery);
        return BackMsg.success(pageInfo);
    }

    @GetMapping("/page/date")
    public BackMsg<Page<Reservation>> pageDate(int page, int pageSize, Date date) {
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Reservation> query = new LambdaQueryWrapper<Reservation>()
                .like(Reservation::getRdate, date)
                .orderByDesc(Reservation::getRdate);
        reservationService.page(pageInfo, query);
        return BackMsg.success(pageInfo);
    }

    @GetMapping("/page/id")
    public BackMsg<Page<Reservation>> pageId(int page, int pageSize, int id) {
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        reservationService.page(
                pageInfo,
                new LambdaQueryWrapper<Reservation>().eq(Reservation::getRuid, id));
        return BackMsg.success(pageInfo);
    }

    @GetMapping("/page")
    public BackMsg<Page<Reservation>> page(int page, int pageSize) {
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        reservationService.page(
                pageInfo,
                new LambdaQueryWrapper<Reservation>().orderByAsc(Reservation::getRid));
        return BackMsg.success(pageInfo);
    }

    @PutMapping("/ban")
    public BackMsg<String> ban(@RequestBody Reservation reservation, int x) {
        if (x == 0) {
            Venue venue = venueService.getOne(new LambdaQueryWrapper<Venue>()
                    .eq(Venue::getVid, reservation.getVenue())
                    .eq(Venue::getFid, reservation.getFacility()));
            if (venue == null) {
                throw new BusinessException("Venue not found.");
            }
            int[] remaining = venueService.remainingCapacity(venue, reservation.getRdate());
            int periodIndex;
            try {
                periodIndex = Integer.parseInt(reservation.getPeriod());
            } catch (NumberFormatException e) {
                throw new BusinessException("Invalid period: " + reservation.getPeriod());
            }
            if (periodIndex < 1 || periodIndex > remaining.length) {
                throw new BusinessException("Period out of range: " + periodIndex);
            }
            reservation.setAmount(remaining[periodIndex - 1]);
            reservation.setRuid(0);
            reservationService.save(reservation);
        } else {
            LambdaQueryWrapper<Reservation> query = new LambdaQueryWrapper<Reservation>()
                    .eq(Reservation::getRuid, 0)
                    .eq(Reservation::getPeriod, reservation.getPeriod())
                    .eq(Reservation::getVenue, reservation.getVenue())
                    .eq(Reservation::getFacility, reservation.getFacility())
                    .eq(Reservation::getRdate, reservation.getRdate());
            Reservation blocking = reservationService.getOne(query);
            if (blocking == null) {
                throw new BusinessException("No blocking reservation found to unblock.");
            }
            blocking.setStatus("unable");
            reservationService.update(blocking, query);
        }
        return BackMsg.success("success");
    }

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
        Venue venue = venueService.getOne(new LambdaQueryWrapper<Venue>().eq(Venue::getVname, name1));
        if (venue == null) {
            throw new BusinessException("Venue not found: " + name1);
        }
        return BackMsg.success(reservationService.list(
                new LambdaQueryWrapper<Reservation>().eq(Reservation::getVenue, venue.getVid())));
    }

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

    @PutMapping("/update")
    public BackMsg<String> edit(@RequestBody Reservation reservation) {
        reservationService.update(
                reservation,
                new LambdaQueryWrapper<Reservation>().eq(Reservation::getRid, reservation.getRid()));
        return BackMsg.success("updated successfully!");
    }

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