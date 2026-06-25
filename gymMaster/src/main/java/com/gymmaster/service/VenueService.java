package com.gymmaster.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gymmaster.entity.VenCap;
import com.gymmaster.entity.Venue;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface VenueService extends IService<Venue> {

    /**
     * Calculate remaining capacity per time-period slot (8 slots, index 0–7)
     * for {@code venue} on the given {@code date}.
     * Slot values represent how many more bookings are still available.
     */
    int[] remainingCapacity(Venue venue, Date date);

    /**
     * Build a VenCap list covering the next {@code days} days for a specific venue.
     */
    List<VenCap> venueCapacityForNextDays(Venue venue, int days);

    /**
     * Build a date-keyed map of VenCap lists for every available venue
     * over the next {@code days} days.
     */
    Map<String, List<VenCap>> allAvailableCapacityForNextDays(int days);
}
