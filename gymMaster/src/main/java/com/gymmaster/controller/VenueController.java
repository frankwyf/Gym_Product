package com.gymmaster.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.VenCap;
import com.gymmaster.entity.Venue;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.VenueService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/venue")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class VenueController {
    private final VenueService venueService;

    @GetMapping(value = "/getById", params = {"vid"})
    public BackMsg<List<VenCap>> getById(int vid) {
        Venue venue = venueService.getById(vid);
        if (venue == null) {
            throw new BusinessException("Venue not found.");
        }
        return BackMsg.success(venueService.venueCapacityForNextDays(venue, 7));
    }

    @GetMapping(value = "/getByName", params = {"vname"})
    public BackMsg<List<VenCap>> getByName(String vname) {
        Venue venue = venueService.getOne(new LambdaQueryWrapper<Venue>().eq(Venue::getVname, vname));
        if (venue == null) {
            throw new BusinessException("Venue not found.");
        }
        return BackMsg.success(venueService.venueCapacityForNextDays(venue, 7));
    }

    @GetMapping("/getAvailableVenues")
    public BackMsg<Map<String, List<VenCap>>> getAvailableVenues() {
        return BackMsg.success(venueService.allAvailableCapacityForNextDays(7));
    }

    @GetMapping(value = "/getFid", params = {"fids"})
    public BackMsg<List<VenCap>> getFid(int fids) {
        List<Venue> venues = venueService.list(new LambdaQueryWrapper<Venue>().eq(Venue::getFid, fids));
        List<VenCap> result = new ArrayList<>();
        for (Venue venue : venues) {
            result.addAll(venueService.venueCapacityForNextDays(venue, 7));
        }
        return BackMsg.success(result);
    }

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
        for (Venue venue : all) {
            result.add(new VenCap(venue, venueService.remainingCapacity(venue, parsedDate)));
        }
        return BackMsg.success(result);
    }

    @PutMapping("/edit")
    public BackMsg<String> edit(@RequestBody Venue venue) {
        LambdaQueryWrapper<Venue> query = new LambdaQueryWrapper<Venue>().eq(Venue::getVid, venue.getVid());
        venueService.update(venue, query);
        return BackMsg.success("Venue updated.");
    }
}