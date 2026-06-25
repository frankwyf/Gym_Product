package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.VenCap;
import com.gymmaster.entity.Venue;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.VenueService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Exposes venue availability data.
 *
 * <p>All capacity-calculation logic has been moved to {@link com.gymmaster.service.impl.VenueServiceImpl},
 * eliminating the 5× duplicated capacity-loop that previously lived in this controller.
 */
@Slf4j
@RestController
@RequestMapping("/venue")
@RequiredArgsConstructor
public class VenueController {
    private final VenueService venueService;

    /** Availability for a specific venue (by ID) over the next 7 days. */
    @GetMapping(value = "/getById", params = {"vid"})
    public BackMsg<List<VenCap>> getById(int vid) {
        Venue venue = venueService.getById(vid);
        if (venue == null) throw new BusinessException("Venue not found.");
        return BackMsg.success(venueService.venueCapacityForNextDays(venue, 7));
    }

    /** Availability for a specific venue (by name) over the next 7 days. */
    @GetMapping(value = "/getByName", params = {"vname"})
    public BackMsg<List<VenCap>> getByName(String vname) {
        Venue venue = venueService.getOne(new LambdaQueryWrapper<Venue>()
                .eq(Venue::getVname, vname));
        if (venue == null) throw new BusinessException("Venue not found.");
        return BackMsg.success(venueService.venueCapacityForNextDays(venue, 7));
    }

    /** Availability for all available venues grouped by date, next 7 days. */
    @GetMapping("/getAvailableVenues")
    public BackMsg<Map<String, List<VenCap>>> getAvailableVenues() {
        return BackMsg.success(venueService.allAvailableCapacityForNextDays(7));
    }

    /** Availability for all venues belonging to a facility (fid) over the next 7 days. */
    @GetMapping(value = "/getFid", params = {"fids"})
    public BackMsg<List<VenCap>> getFid(int fids) {
        List<Venue> venues = venueService.list(new LambdaQueryWrapper<Venue>()
                .eq(Venue::getFid, fids));
        List<VenCap> result = new ArrayList<>();
        for (Venue v : venues) {
            result.addAll(venueService.venueCapacityForNextDays(v, 7));
        }
        return BackMsg.success(result);
    }

    /** Availability for all venues on a specific calendar date. */
    @GetMapping(value = "/getDate", params = {"date"})
    public BackMsg<List<VenCap>> getDate(String date) {
        java.util.Date parsedDate;
        try {
            parsedDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (ParseException e) {
            throw new BusinessException("Invalid date format. Expected yyyy-MM-dd.");
        }
        List<Venue> all = venueService.list(null);
        List<VenCap> result = new ArrayList<>();
        for (Venue v : all) {
            result.add(new VenCap(v, venueService.remainingCapacity(v, parsedDate)));
        }
        return BackMsg.success(result);
    }

    @PutMapping("/edit")
    public BackMsg<String> edit(@RequestBody Venue venue) {
        LambdaQueryWrapper<Venue> qw = new LambdaQueryWrapper<Venue>()
                .eq(Venue::getVid, venue.getVid());
        venueService.update(venue, qw);
        return BackMsg.success("Venue updated.");
    }
}
