package com.gym.project.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.project.gymmaster.common.BackMsg;
import com.gym.project.gymmaster.entity.Reservation;
import com.gym.project.gymmaster.entity.VenCap;
import com.gym.project.gymmaster.entity.Venue;
import com.gym.project.gymmaster.service.ReservationService;
import com.gym.project.gymmaster.service.VenueService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@Slf4j
@RestController
@RequestMapping("/venue")
public class VenueController {
    @Autowired
    VenueService venueService;
    @Autowired
    ReservationService reservationService;
    @GetMapping("/getById")
    public BackMsg getById(@RequestBody Venue venue){
        Date currentDate = new Date(System.currentTimeMillis());
        ArrayList<Date> next7Days = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
//            Calendar c = Calendar.getInstance();
//
//            java.util.Date date = c.getTime();
//            java.sql.Date d = new java.sql.Date(date.getTime());
            //next7Days.add(new Date(currentDate.getTime() + i * 24 * 60 * 60 * 1000));
            Date current = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(current);
            calendar.add(calendar.DATE, i);
            Date date = calendar.getTime();
            java.sql.Date d = new java.sql.Date(date.getTime());
            next7Days.add(d);

        }
        List<VenCap> venCaps = new ArrayList<>();
        LambdaQueryWrapper<Venue> venueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        venueLambdaQueryWrapper.eq(Venue::getVid,venue.getVid());
        Venue venueThis = venueService.getOne(venueLambdaQueryWrapper);
        int capacity = venueThis.getCapacity();
        for(Date date:next7Days) {

            LambdaQueryWrapper<Reservation> reservationLambdaQueryWrapper = new LambdaQueryWrapper<>();
            reservationLambdaQueryWrapper.eq(Reservation::getRdate, date)
                    .eq(Reservation::getFacility, venueThis.getFid())
                    .eq(Reservation::getVenue, venue.getVid())
                    .eq(Reservation::getStatus, "valid");
            ArrayList<Reservation> reservations = new ArrayList<>();
            //put all reservations in reservationLambdaQueryWrapper into the arraylist
            for (Reservation reservation : reservationService.list(reservationLambdaQueryWrapper)) {
                reservations.add(reservation);
            }
//            LambdaQueryWrapper<Venue> cap = new LambdaQueryWrapper<>();
//            cap.eq(Venue::getVid,venue.getVid())
//                    .eq(Venue::getFid, venue.getFid());
//            Venue venue1 = venueService.getOne(cap);


            //3. 统计每一个时段的人数
            int [] curCap= new int[8];//
            for(Reservation res: reservations){
                String [] per1 = null;
                int [] per = null;
                //获得this预约的period
                per1 = res.getPeriod().split(",");
                per = new int[per1.length];
                for (int i = 0;i<per.length;i++){
                    per[i] = Integer.parseInt(per1[i]);
                }
                //更新该时段已预约的总数
                for (int eachPeriod: per){
                    curCap[eachPeriod-1] +=res.getAmount();
                }
            }
            //4. 获取每一时间段的预约总量与venue capacity的差值，并放入map中 以 venue名为key
            for (int i = 0;i<curCap.length;i++){
                curCap[i] = venueThis.getCapacity() - curCap[i];
            }
            venCaps.add(new VenCap(venueThis,date,curCap));
        }
        return BackMsg.success(venCaps);
    }

    @GetMapping("/getAvailableVenues")
    public BackMsg<List> getAvailableVenues() {
        // get the current date and make an array with the next 7 days
        Date currentDate = new Date(System.currentTimeMillis());
        ArrayList<Date> next7Days = new ArrayList<>();

        for (int i = 0; i < 7; i++) {
            Date current = new Date();
            Calendar calendar = new GregorianCalendar();
            calendar.setTime(current);
            calendar.add(calendar.DATE, i);
            Date date = calendar.getTime();
            java.sql.Date d = new java.sql.Date(date.getTime());
            next7Days.add(d);

        }

        LambdaQueryWrapper<Venue> venueLambdaQueryWrapper = new LambdaQueryWrapper<>();
        ArrayList<Venue> venues = new ArrayList<>();
        // put all venues into the arraylist
        // select the ones that are available
        for (Venue venue : venueService.list(venueLambdaQueryWrapper)) {
            venues.add(venue);
        }
//        ArrayList<ArrayList<Reservation>> availableVenues = new ArrayList<>();

        List<VenCap> venCaps = new ArrayList<>();
        for (Venue venue: venues){
            for(Date date:next7Days) {

                LambdaQueryWrapper<Reservation> reservationLambdaQueryWrapper = new LambdaQueryWrapper<>();
                reservationLambdaQueryWrapper.eq(Reservation::getRdate, date)
                        .eq(Reservation::getFacility, venue.getFid())
                        .eq(Reservation::getVenue, venue.getVid())
                        .eq(Reservation::getStatus, "valid");
                ArrayList<Reservation> reservations = new ArrayList<>();
                //put all reservations in reservationLambdaQueryWrapper into the arraylist
                for (Reservation reservation : reservationService.list(reservationLambdaQueryWrapper)) {
                    reservations.add(reservation);
                }
                LambdaQueryWrapper<Venue> cap = new LambdaQueryWrapper<>();
                cap.eq(Venue::getVid,venue.getVid())
                        .eq(Venue::getFid, venue.getFid());
                Venue venue1 = venueService.getOne(cap);
                int capacity = venue.getCapacity();

                //3. 统计每一个时段的人数
                int [] curCap= new int[8];//
                for(Reservation res: reservations){
                    String [] per1 = null;
                    int [] per = null;
                    //获得this预约的period
                    per1 = res.getPeriod().split(",");
                    per = new int[per1.length];
                    for (int i = 0;i<per.length;i++){
                        per[i] = Integer.parseInt(per1[i]);
                    }
                    //更新该时段已预约的总数
                    for (int eachPeriod: per){
                        curCap[eachPeriod-1] +=res.getAmount();
                    }
                }
                //4. 获取每一时间段的预约总量与venue capacity的差值，并放入map中 以 venue名为key
                for (int i = 0;i<curCap.length;i++){
                    curCap[i] = venue1.getCapacity() - curCap[i];
                }
                venCaps.add(new VenCap(venue1,date,curCap));
            }
        }
        // return the result
        return BackMsg.success(venCaps);
    }


}
