package com.gym.project.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gym.project.gymmaster.common.BackMsg;
import com.gym.project.gymmaster.entity.Customer;
import com.gym.project.gymmaster.entity.Reservation;
import com.gym.project.gymmaster.entity.Venue;
import com.gym.project.gymmaster.service.CustomerService;
import com.gym.project.gymmaster.service.ReservationService;
import com.gym.project.gymmaster.service.VenueService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/reservation")
public class ReservationController {
    @Autowired
    ReservationService reservationService;
    @Autowired
    CustomerService customerService;
    @Autowired
    VenueService venueService;
    @GetMapping("/page")
    public BackMsg<Page<Reservation>> page(int page, int pageSize, String name, Date date, int id){

//        int page1 = Integer.parseInt(page);
//        int pageSize1 = Integer.parseInt(pageSize);
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        if(date == null && name != null) {
            LambdaQueryWrapper<Customer> queryWrapper0 = new LambdaQueryWrapper<>();
            queryWrapper0.like(StringUtils.isNotEmpty(name), Customer::getUsername, name);

            Customer customer = customerService.getOne(queryWrapper0);

            LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(StringUtils.isNotEmpty(name), Reservation::getRuid, customer.getUid());
            queryWrapper.orderByDesc(Reservation::getRdate);
            reservationService.page(pageInfo, queryWrapper);
        }
        else if(name == null && date !=null){
            LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Reservation::getRdate, date);
            queryWrapper.orderByDesc(Reservation::getRuid);
            reservationService.page(pageInfo, queryWrapper);
        }
        else if(name != null && date!=null){
            LambdaQueryWrapper<Customer> queryWrapper0 = new LambdaQueryWrapper<>();
            queryWrapper0.like(StringUtils.isNotEmpty(name), Customer::getUsername, name);

            Customer customer = customerService.getOne(queryWrapper0);

            LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(StringUtils.isNotEmpty(name), Reservation::getRuid, customer.getUid());
            queryWrapper.eq(Reservation::getRdate,date);
            reservationService.page(pageInfo, queryWrapper);
        }
        else if(id !=0){
            LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(Reservation::getRuid,id);

            reservationService.page(pageInfo,queryWrapper);
        }
        else{
            LambdaQueryWrapper<Reservation> queryWrapper0 = new LambdaQueryWrapper<>();
            queryWrapper0.orderByAsc(Reservation::getRid);
            reservationService.page(pageInfo,queryWrapper0);
        }
        return BackMsg.success(pageInfo);
    }


//
    @PostMapping("/add")
    public BackMsg<String> add(@RequestBody Reservation reservation){
        // 查询已有的该时段记录
        //1. 查找该场馆该场地该日的所有valid reservation
        LambdaQueryWrapper<Reservation> exist = new LambdaQueryWrapper<>();
        exist.eq(Reservation::getRdate,reservation.getRdate())
                .eq(Reservation::getFacility,reservation.getFacility())
                .eq(Reservation::getVenue,reservation.getVenue())
                .eq(Reservation::getStatus,"valid");
        List<Reservation> periods = reservationService.list(exist);

        //2. 获取该vid下的capacity
        LambdaQueryWrapper<Venue> cap = new LambdaQueryWrapper<>();
        cap.eq(Venue::getVid,reservation.getVenue())
                .eq(Venue::getFid, reservation.getFacility());
        Venue venue = venueService.getOne(cap);
        int capacity = venue.getCapacity();

        //3. 统计每一个时段的人数
        int [] curCap= new int[8];
        for(Reservation res: periods){
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
        //判断如果这个预约加进去会不会超额
        String [] thisR = reservation.getPeriod().split(",");
        int [] thisRP = new int[thisR.length];
        for (int i = 0;i<thisR.length;i++){
            thisRP[i] = Integer.parseInt(thisR[i]);
        }
        for (int thisPeriod: thisRP){
            if (thisPeriod>8 || thisPeriod<0){
                return BackMsg.error("wrong period!");
            }
            if (curCap[thisPeriod-1]+reservation.getAmount()>capacity){
                String er = thisPeriod + "has left less than " + reservation.getAmount() +
                        " capacity. Thus, reservation for all periods haven't been reserved successfully!";
                return BackMsg.error(er);
            }
        }
        //todo 模拟付费
        reservation.setStatus("valid");
        reservationService.save(reservation);
        return BackMsg.success("reservation added successfully!");
    }
    @PutMapping("/update")
    public BackMsg<String> edit(@RequestBody Reservation reservation){
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reservation::getRid,reservation.getRid());
        reservationService.update(reservation,queryWrapper);
        return BackMsg.success("updated successfully!");
    }
}
