package com.gymmaster.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gymmaster.entity.Reservation;
import com.gymmaster.entity.VenCap;
import com.gymmaster.entity.Venue;
import com.gymmaster.mapper.VenueMapper;
import com.gymmaster.service.ReservationService;
import com.gymmaster.service.VenueService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null")
public class VenueServiceImpl extends ServiceImpl<VenueMapper, Venue> implements VenueService {

    private static final int TIME_PERIODS = 8;

    // @Lazy to break circular dependency (ReservationService may depend on VenueService)
    @Lazy
    private final ReservationService reservationService;

    @Override
    public int[] remainingCapacity(Venue venue, Date date) {
        LambdaQueryWrapper<Reservation> qw = new LambdaQueryWrapper<Reservation>()
                .eq(Reservation::getRdate, date)
                .eq(Reservation::getFacility, venue.getFid())
                .eq(Reservation::getVenue, venue.getVid())
                .eq(Reservation::getStatus, "valid");
        List<Reservation> reservations = reservationService.list(qw);

        int[] used = new int[TIME_PERIODS];
        for (Reservation res : reservations) {
            if (res.getPeriod() == null) continue;
            for (String slot : res.getPeriod().split(",")) {
                try {
                    int idx = Integer.parseInt(slot.trim()) - 1;
                    if (idx >= 0 && idx < TIME_PERIODS) {
                        used[idx] += res.getAmount();
                    }
                } catch (NumberFormatException ignored) { }
            }
        }

        int[] remaining = new int[TIME_PERIODS];
        for (int i = 0; i < TIME_PERIODS; i++) {
            remaining[i] = venue.getCapacity() - used[i];
        }
        return remaining;
    }

    @Override
    public List<VenCap> venueCapacityForNextDays(Venue venue, int days) {
        List<VenCap> result = new ArrayList<>();
        for (java.sql.Date date : nextDays(days)) {
            result.add(new VenCap(venue, remainingCapacity(venue, date)));
        }
        return result;
    }

    @Override
    public Map<String, List<VenCap>> allAvailableCapacityForNextDays(int days) {
        List<Venue> available = list(new LambdaQueryWrapper<Venue>()
                .eq(Venue::getStatus, "available"));
        Map<String, List<VenCap>> result = new LinkedHashMap<>();
        for (java.sql.Date date : nextDays(days)) {
            List<VenCap> venCaps = new ArrayList<>();
            for (Venue v : available) {
                venCaps.add(new VenCap(v, remainingCapacity(v, date)));
            }
            result.put(date.toString(), venCaps);
        }
        return result;
    }

    // --- private helpers ---

    private static List<java.sql.Date> nextDays(int count) {
        List<java.sql.Date> dates = new ArrayList<>();
        Calendar cal = Calendar.getInstance();
        for (int i = 0; i < count; i++) {
            dates.add(new java.sql.Date(cal.getTimeInMillis()));
            cal.add(Calendar.DATE, 1);
        }
        return dates;
    }
}
