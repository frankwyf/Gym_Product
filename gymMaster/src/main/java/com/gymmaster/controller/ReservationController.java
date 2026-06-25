package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Customer;
import com.gymmaster.entity.Reservation;
import com.gymmaster.entity.Venue;
import com.gymmaster.qr.QrCodeUtils;
import com.gymmaster.service.CustomerService;
import com.gymmaster.service.ReservationService;
import com.gymmaster.service.VenueService;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
    @GetMapping("/page/username")
    public BackMsg<Page<Reservation>> pageUsername(int page, int pageSize, String name) {
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Customer> cqw = new LambdaQueryWrapper<Customer>()
                .like(StringUtils.isNotEmpty(name), Customer::getUsername, name);
        Customer customer = customerService.getOne(cqw);
        if (customer == null) {
            // No matching customer — return empty page rather than NPE.
            return BackMsg.success(pageInfo);
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
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.like(Reservation::getRdate, date);
            queryWrapper.orderByDesc(Reservation::getRuid);
            reservationService.page(pageInfo, queryWrapper);
        return BackMsg.success(pageInfo);
    }
    @GetMapping("/page/id")
    public BackMsg<Page<Reservation>> pageId(int page, int pageSize, int id) {
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();

            queryWrapper.eq(Reservation::getRuid,id);

            reservationService.page(pageInfo,queryWrapper);
            return BackMsg.success(pageInfo);
    }

    @GetMapping("/page")
    public BackMsg<Page<Reservation>> page(int page, int pageSize){
        Page<Reservation> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Reservation> queryWrapper0 = new LambdaQueryWrapper<>();
        queryWrapper0.orderByAsc(Reservation::getRid);
        reservationService.page(pageInfo,queryWrapper0);
        return BackMsg.success(pageInfo);
    }

    @PutMapping("/ban")
    public BackMsg<String> ban(@RequestBody Reservation reservation, int x ){
        if(x==0) {
            LambdaQueryWrapper<Reservation> exist = new LambdaQueryWrapper<>();
            exist.eq(Reservation::getRdate, reservation.getRdate())
                    .eq(Reservation::getFacility, reservation.getFacility())
                    .eq(Reservation::getVenue, reservation.getVenue())
                    .eq(Reservation::getStatus, "valid");
            List<Reservation> periods = reservationService.list(exist);

            LambdaQueryWrapper<Venue> cap = new LambdaQueryWrapper<>();
            cap.eq(Venue::getVid, reservation.getVenue())
                    .eq(Venue::getFid, reservation.getFacility());
            Venue venue = venueService.getOne(cap);
            int capacity = venue.getCapacity();

            int[] curCap = new int[8];
            for (Reservation res : periods) {
                String[] per1 = res.getPeriod().split(",");
                int[] per = new int[per1.length];
                for (int i = 0; i < per.length; i++) {
                    per[i] = Integer.parseInt(per1[i]);
                }
                for (int eachPeriod : per) {
                    curCap[eachPeriod - 1] += res.getAmount();
                }
            }
            reservation.setAmount(capacity - curCap[Integer.parseInt(reservation.getPeriod())]);
            reservation.setRuid(0);
            reservationService.save(reservation);
            return BackMsg.success("success");
        }
        else {
            LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Reservation::getRuid,0)
                    .eq(Reservation::getPeriod,reservation.getPeriod())
                    .eq(Reservation::getVenue,reservation.getVenue())
                    .eq(Reservation::getFacility,reservation.getFacility())
                    .eq(Reservation::getRdate,reservation.getRdate());
            Reservation reservation1 = reservationService.getOne(queryWrapper);
            reservation1.setStatus("unable");
            reservationService.update(reservation1,queryWrapper);
        }
        return BackMsg.success("success");
    }

    @GetMapping("/findId")
    public BackMsg<Reservation> find(int id){
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reservation::getRid,id);
        Reservation reservation = reservationService.getOne(queryWrapper);
        return BackMsg.success(reservation);
    }
    @GetMapping("/findVid")
    public BackMsg<List<Reservation>> findvid(int id){
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reservation::getVenue,id);
        return BackMsg.success(reservationService.list(queryWrapper));
    }
    @GetMapping("/findvname")
    public BackMsg<List<Reservation>> find(String name1){
        LambdaQueryWrapper<Venue> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Venue::getVname,name1);
        Venue venue = venueService.getOne(queryWrapper);
        LambdaQueryWrapper<Reservation> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Reservation::getVenue, venue.getVid());
        return BackMsg.success(reservationService.list(queryWrapper1));
    }
    @PostMapping("/add")
    public BackMsg<String> add(@RequestBody Reservation reservation, HttpServletRequest request) throws Exception {
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

        int [] curCap= new int[8];
        for(Reservation res: periods){
            String [] per1 = res.getPeriod().split(",");
            int [] per = new int[per1.length];
            for (int i = 0;i<per.length;i++){
                per[i] = Integer.parseInt(per1[i]);
            }
            for (int eachPeriod: per){
               curCap[eachPeriod-1] +=res.getAmount();
            }
        }
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
        String token = request.getHeader("token");
        // analyse token
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            log.error("illegal token in /reservation/add", e);
            throw  new RuntimeException("illegal token");
        }
        int id = Integer.parseInt(userid);
        reservation.setStatus("unpaid");
        reservation.setRuid(id);
        reservationService.save(reservation);


        String logoPath = "src/main/resources/static/logo/logo.png";
        String destPath = "src/main/resources/static/reservationQR/"+reservation.getRid()+".jpg";
        QrCodeUtils.encode(reservation.toString(),logoPath,destPath,true);


        return BackMsg.success("reservation added successfully!");
    }
    @Autowired
    RedisCache redisCache;
    @GetMapping("/getUnpaid")
    public BackMsg<List<Reservation>> getUnpaid(HttpServletRequest request){
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
        String token = request.getHeader("token");
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            log.error("illegal token in /reservation/getUnpaid", e);
            throw  new RuntimeException("illegal token");
        }
        String redisKey = "login"+userid;
        // keep redis touch to preserve login-state behavior
        redisCache.getCacheObject(redisKey);
        int uid = Integer.parseInt(userid);
        queryWrapper.eq(Reservation::getRuid, uid)
                .eq(Reservation::getStatus,"unpaid");

        return BackMsg.success(reservationService.list(queryWrapper));
    }
    @GetMapping("/getPaid")
    public BackMsg<List<Reservation>> getPaid(HttpServletRequest request){
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
        String token = request.getHeader("token");
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            log.error("illegal token in /reservation/getPaid", e);
            throw  new RuntimeException("illegal token");
        }
        int uid = Integer.parseInt(userid);
        queryWrapper.eq(Reservation::getRuid, uid)
                .eq(Reservation::getStatus,"valid");
        return BackMsg.success(reservationService.list(queryWrapper));
    }

    @PutMapping("/update")
    public BackMsg<String> edit(@RequestBody Reservation reservation){
        LambdaQueryWrapper<Reservation> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Reservation::getRid,reservation.getRid());
        reservationService.update(reservation,queryWrapper);
        return BackMsg.success("updated successfully!");
    }

    @PostMapping("/add/management")
    public BackMsg<String> addManagement(@RequestBody Reservation reservation) throws Exception {
        LambdaQueryWrapper<Reservation> exist = new LambdaQueryWrapper<>();
        exist.eq(Reservation::getRdate,reservation.getRdate())
                .eq(Reservation::getFacility,reservation.getFacility())
                .eq(Reservation::getVenue,reservation.getVenue())
                .eq(Reservation::getStatus,"valid");
        List<Reservation> periods = reservationService.list(exist);

        LambdaQueryWrapper<Venue> cap = new LambdaQueryWrapper<>();
        cap.eq(Venue::getVid,reservation.getVenue())
                .eq(Venue::getFid, reservation.getFacility());
        Venue venue = venueService.getOne(cap);
        int capacity = venue.getCapacity();

        int [] curCap= new int[8];
        for(Reservation res: periods){
            String [] per1 = res.getPeriod().split(",");
            int [] per = new int[per1.length];
            for (int i = 0;i<per.length;i++){
                per[i] = Integer.parseInt(per1[i]);
            }
            for (int eachPeriod: per){
                curCap[eachPeriod-1] +=res.getAmount();
            }
        }
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

        reservation.setStatus("unpaid");

        reservationService.save(reservation);


        String logoPath = "src/main/resources/static/logo/logo.png";
        String destPath = "src/main/resources/static/reservationQR/"+reservation.getRid()+".jpg";
        QrCodeUtils.encode(reservation.toString(),logoPath,destPath,true);


        return BackMsg.success("reservation added successfully!");
    }
}
