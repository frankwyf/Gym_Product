package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Reservation;
import com.gymmaster.entity.VenCap;
import com.gymmaster.entity.Venue;
import com.gymmaster.service.ReservationService;
import com.gymmaster.service.VenueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
@RestController
@RequestMapping("/venue")
public class VenueController {
    @Autowired
    VenueService venueService;
    @Autowired
    ReservationService reservationService;
    @GetMapping(value = "/getById", params = {"vid"})
    public BackMsg<List<VenCap>> getById(int vid){
        ArrayList<Date> next7Days = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Date current = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, i);
            Date date = calendar.getTime();
            java.sql.Date d = new java.sql.Date(date.getTime());
            next7Days.add(d);
        }

        List<VenCap> venCaps = new ArrayList<>();
        LambdaQueryWrapper<Venue> venueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        venueLambdaQueryWrapper.eq(Venue::getVid,vid);
        Venue venueThis = venueService.getOne(venueLambdaQueryWrapper);

        for(Date date:next7Days) {
            LambdaQueryWrapper<Reservation> reservationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            reservationLambdaQueryWrapper.eq(Reservation::getRdate, date)
                    .eq(Reservation::getFacility, venueThis.getFid())
                    .eq(Reservation::getVenue, vid)
                    .eq(Reservation::getStatus, "valid");
            List<Reservation> reservations = reservationService.list(reservationLambdaQueryWrapper);


            //3. statistics the capacity of the venue for each time period
            int [] curCap= new int[8];//
            for(Reservation res: reservations){
                String [] per1 = res.getPeriod().split(",");
                int [] per = new int[per1.length];
                for (int i = 0;i<per.length;i++){
                    per[i] = Integer.parseInt(per1[i]);
                }
                // update the capacity of the venue for each time period
                for (int eachPeriod: per){
                    curCap[eachPeriod-1] +=res.getAmount();
                }
            }
            // get the difference between the total amount of reservations and the capacity of the venue for each time period
            // and put it into the map with the venue name as the key
            for (int i = 0;i<curCap.length;i++){
                curCap[i] = venueThis.getCapacity() - curCap[i];
            }
            venCaps.add(new VenCap(venueThis,curCap));
        }
        return BackMsg.success(venCaps);
    }
    @GetMapping(value = "/getByName", params = {"vid"})
    public BackMsg<List<VenCap>> getByName(String vid){
        ArrayList<Date> next7Days = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Date current = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, i);
            Date date = calendar.getTime();
            java.sql.Date d = new java.sql.Date(date.getTime());
            next7Days.add(d);
        }

        List<VenCap> venCaps = new ArrayList<>();
        LambdaQueryWrapper<Venue> venueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        venueLambdaQueryWrapper.eq(Venue::getVname,vid);
        Venue venueThis = venueService.getOne(venueLambdaQueryWrapper);

        for(Date date:next7Days) {
            LambdaQueryWrapper<Reservation> reservationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            reservationLambdaQueryWrapper.eq(Reservation::getRdate, date)
                    .eq(Reservation::getFacility, venueThis.getFid())
                    .eq(Reservation::getVenue, venueThis.getVid())
                    .eq(Reservation::getStatus, "valid");
            List<Reservation> reservations = reservationService.list(reservationLambdaQueryWrapper);


            //3. statistics the capacity of the venue for each time period
            int [] curCap= new int[8];//
            for(Reservation res: reservations){
                String [] per1 = res.getPeriod().split(",");
                int [] per = new int[per1.length];
                for (int i = 0;i<per.length;i++){
                    per[i] = Integer.parseInt(per1[i]);
                }
                // update the capacity of the venue for each time period
                for (int eachPeriod: per){
                    curCap[eachPeriod-1] +=res.getAmount();
                }
            }
            // get the difference between the total amount of reservations and the capacity of the venue for each time period
            // and put it into the map with the venue name as the key
            for (int i = 0;i<curCap.length;i++){
                curCap[i] = venueThis.getCapacity() - curCap[i];
            }
            venCaps.add(new VenCap(venueThis,curCap));
        }
        return BackMsg.success(venCaps);
    }

    @GetMapping("/getAvailableVenues")
    public BackMsg<Map<String, List<VenCap>>> getAvailableVenues() {
        ArrayList<Date> next7Days = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Date current = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, i);
            Date date = calendar.getTime();
            java.sql.Date d = new java.sql.Date(date.getTime());
            next7Days.add(d);

        }

        LambdaQueryWrapper<Venue> venueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        List<Venue> venues = new ArrayList<>();
        for (Venue venue : venueService.list(venueLambdaQueryWrapper)) {
            // select the ones that are available
            if (venue.getStatus().equals("available"))
            venues.add(venue);
        }
        Map<String, List<VenCap>> dateListMap = new LinkedHashMap<>();

        for (Date date:next7Days){
            List<VenCap> venCaps = new ArrayList<>();
            for(Venue venue: venues){
                LambdaQueryWrapper<Reservation> reservationLambdaQueryWrapper = new LambdaQueryWrapper<>();
                reservationLambdaQueryWrapper.eq(Reservation::getRdate, date)
                        .eq(Reservation::getFacility, venue.getFid())
                        .eq(Reservation::getVenue, venue.getVid())
                        .eq(Reservation::getStatus, "valid");
                List<Reservation> reservations = reservationService.list(reservationLambdaQueryWrapper);
                LambdaQueryWrapper<Venue> cap = new LambdaQueryWrapper<>();
                cap.eq(Venue::getVid,venue.getVid())
                        .eq(Venue::getFid, venue.getFid());
                Venue venue1 = venueService.getOne(cap);

                //3. statistics the number of people in each time period
                int [] curCap= new int[8];//
                for(Reservation res: reservations){
                    String [] per1 = res.getPeriod().split(",");
                    int [] per = new int[per1.length];
                    for (int i = 0;i < per.length;i++){
                        per[i] = Integer.parseInt(per1[i]);
                    }
                    //update the capacity of the venue for each time period
                    for (int eachPeriod: per){
                        curCap[eachPeriod-1] +=res.getAmount();
                    }
                }
                //4. get the difference between the total amount of reservations and the capacity of the venue for each time period
                // and put it into the map with the venue name as the key
                for (int i = 0;i<curCap.length;i++){
                    curCap[i] = venue1.getCapacity() - curCap[i];
                }
                venCaps.add(new VenCap(venue1,curCap));
            }
            dateListMap.put(date.toString(),venCaps);
        }
        // return the result
        return BackMsg.success(dateListMap);
    }
    @GetMapping(value = "/getFid",params = {"fids"})
    public BackMsg<List<VenCap>> getFid(int fids) {
        ArrayList<Date> next7Days = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Date current = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(current);
            calendar.add(Calendar.DATE, i);
            Date date = calendar.getTime();
            java.sql.Date d = new java.sql.Date(date.getTime());
            next7Days.add(d);
        }

        List<VenCap> venCaps = new ArrayList<>();
        LambdaQueryWrapper<Venue> venueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        venueLambdaQueryWrapper.eq(Venue::getFid,fids);
        List<Venue> venues = venueService.list(venueLambdaQueryWrapper);
        //Venue venueThis = venueService.getOne(venueLambdaQueryWrapper);
        //int capacity = venueThis.getCapacity();

        for(Venue venueThis: venues) {
            for(Date date:next7Days){
                LambdaQueryWrapper<Reservation> reservationLambdaQueryWrapper = new LambdaQueryWrapper<>();
                reservationLambdaQueryWrapper.eq(Reservation::getRdate, date)
                        .eq(Reservation::getFacility, venueThis.getFid())
                        .eq(Reservation::getVenue, fids)
                        .eq(Reservation::getStatus, "valid");
                List<Reservation> reservations = reservationService.list(reservationLambdaQueryWrapper);


                //3. statistics the capacity of the venue for each time period
                int[] curCap = new int[8];//
                for (Reservation res : reservations) {
                    String[] per1 = res.getPeriod().split(",");
                    int[] per = new int[per1.length];
                    for (int i = 0; i < per.length; i++) {
                        per[i] = Integer.parseInt(per1[i]);
                    }
                    // update the capacity of the venue for each time period
                    for (int eachPeriod : per) {
                        curCap[eachPeriod - 1] += res.getAmount();
                    }
                }
                // get the difference between the total amount of reservations and the capacity of the venue for each time period
                // and put it into the map with the venue name as the key
                for (int i = 0; i < curCap.length; i++) {
                    curCap[i] = venueThis.getCapacity() - curCap[i];
                }
                venCaps.add(new VenCap(venueThis, curCap));
            }
        }
        return BackMsg.success(venCaps);
    }


    @GetMapping(value = "/getDate", params = {"Date"})
    public BackMsg<List<VenCap>> getDate(String Date){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        try {
            date = sdf.parse(Date);
        }
        catch (ParseException e) {
            log.error("invalid date parameter: {}", Date, e);
        }

        List<VenCap> venCaps = new ArrayList<>();
        LambdaQueryWrapper<Venue> queryWrapper1 = new LambdaQueryWrapper<>();
        for (Venue venue: venueService.list(queryWrapper1)){
            LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Reservation::getRdate,date).eq(Reservation::getVenue,venue.getVid()).eq(Reservation::getStatus,"valid");
            List<Reservation> reservations = reservationService.list(queryWrapper);
            int [] curCap= new int[8];//
            for(Reservation res: reservations){
                String [] per1 = res.getPeriod().split(",");
                int [] per = new int[per1.length];
                for (int i = 0;i<per.length;i++){
                    per[i] = Integer.parseInt(per1[i]);
                }
                // update the capacity of the venue for each time period
                for (int eachPeriod: per){
                    curCap[eachPeriod-1] +=res.getAmount();
                }
            }
            // get the difference between the total amount of reservations and the capacity of the venue for each time period
            // and put it into the map with the venue name as the key
            for (int i = 0;i<curCap.length;i++){
                curCap[i] = venue.getCapacity() - curCap[i];
            }
            venCaps.add(new VenCap(venue,curCap));
        }
        return BackMsg.success(venCaps);




    }


    @PutMapping("/edit")
    public BackMsg<String> edit(@RequestBody Venue venue){

        LambdaQueryWrapper<Venue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Venue::getVid,venue.getVid());
        venueService.update(venue,queryWrapper);

        return BackMsg.success("success");
    }
}
