package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.*;
import com.gymmaster.qr.QrCodeUtils;
import com.gymmaster.service.*;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;

@RestController
@RequestMapping("/bill")
public class BillController {
    /* implement the following interface */
    @Autowired
    BillService billService;
    @Autowired
    FacilityService facilityService;
    //FacilityService
    //按照种类查看流水，按照月份、周查看流水，
    @GetMapping("/page/period")
    public BackMsg<Map> pagePeriod(Timestamp start, Timestamp endTime) {
        if(start == null){
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Calendar c = Calendar.getInstance();
            c.set(2002,Calendar.JUNE,27);
            java.util.Date date = c.getTime();
            Date d = new Date(date.getTime());

            String time= df.format(d);

            start = Timestamp.valueOf(time);
        }
        if (endTime == null){
            //若无此时间则设置默认为当前时间
            endTime = new Timestamp(System.currentTimeMillis());
        }
        if(start.after(endTime)){
            return BackMsg.error("wrong date entered!");
        }

        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper();
        List<Bill> orders = billService.list(queryWrapper);

//        LambdaQueryWrapper<Facility> queryWrapper0 = new LambdaQueryWrapper();
//        List<Facility> variety = facilityService.list(queryWrapper0);

        Timestamp finalStart = start;
        orders.removeIf(bill -> bill.getBdate().before(finalStart)); //获取了所有时间的
        Timestamp finalEndTime = endTime;
        orders.removeIf(bill -> bill.getBdate().after(finalEndTime));

        Map<String, BigDecimal> statistic = new HashMap<>();
        for( Bill bill: orders ){
            if (!statistic.containsKey(bill.getFname())){
                statistic.put(bill.getFname(),bill.getFigure());
            }
            else{
                statistic.put(bill.getFname(), bill.getFigure().add(statistic.get(bill.getFname())));
            }
        }


        return BackMsg.success(statistic);
    }
    @GetMapping("/page/facility")
    public BackMsg<Page> page(int page, int pageSize, String name){

        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Bill::getFname,name);
        queryWrapper.orderByDesc(Bill::getBid);
        billService.page(pageInfo,queryWrapper);
        return BackMsg.success(pageInfo);
    }
    @GetMapping("/showall")
    public BackMsg showall(HttpServletRequest request){
        String token = request.getHeader("token");
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw  new RuntimeException("illegal token");
        }
        String redisKey = "login"+userid;
        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);
        Customer customer = user.getCustomer();
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Bill::getUid,customer.getUid());
        queryWrapper.orderByDesc(Bill::getBid);
        return BackMsg.success(billService.list(queryWrapper));
    }
    @Autowired
    VenueService venueService;
    @Autowired
    RedisCache redisCache;
    @Autowired
    AccountService accountService;
    @Autowired
    ReservationService reservationService;

    @PostMapping("/pay")
    public BackMsg add(@RequestBody Map<String,Object> goodlist, int aid, double total,HttpServletRequest request) throws Exception {
        // Get the list of values from the map
        List<Goods> goods = new ArrayList<>();
        Map<String, Object> goodsList = goodlist;
        List<Map<String, Object>> goodsMaps = (List<Map<String, Object>>) goodsList.get("goodlist");
        for (Map<String, Object> item : goodsMaps) {
            String date = (String) item.get("date");
            String facility = (String) item.get("facility").toString();
            String venue = (String) item.get("venue").toString();
            String period = (String) item.get("period").toString();
            String amount = (String) item.get("amount").toString();
            String type = (String) item.get("type");
            String pic = (String) item.get("pic");
            String name = (String) item.get("name");
            String price = (String) item.get("price").toString();
            boolean active = (boolean) item.get("active");
            Goods good = new Goods();
            good.setPeriod(period);
            good.setFacility(Integer.parseInt(facility));
            good.setVenue(Integer.parseInt(venue));
            good.setAmount(Integer.parseInt(amount));
            good.setPrice(Integer.parseInt(price));
            good.setActive(active);
            good.setDate(date);
            good.setPic(pic);
            good.setName(name);
            good.setType(type);
            goods.add(good);
        }

        String token = request.getHeader("token");
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("illegal token");
        }
        String redisKey = "login" + userid;

        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);
        List<Reservation> reservations = new ArrayList<>();
        for(Goods good: goods){
            Reservation reservation = new Reservation();
            reservation.setRuid(Integer.parseInt(userid));
            reservation.setAmount(good.getAmount());
            reservation.setPeriod(good.getPeriod());
            reservation.setFacility(good.getFacility());
            reservation.setVenue(good.getVenue());
            reservation.setStatus("unpaid");

            String dd;
            if (good.getDate().length()>5){
                dd = good.getDate().substring(0,10);
            }
            else {
                dd = "2023-"+good.getDate();
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date date = sdf.parse(dd);
            Date d = new Date(date.getTime());
            reservation.setRdate(d);
            reservation.setPayment("account");

            LambdaQueryWrapper<Reservation> exist = new LambdaQueryWrapper<>();
            exist.eq(Reservation::getRdate,reservation.getRdate())
                    .eq(Reservation::getFacility,reservation.getFacility())
                    .eq(Reservation::getVenue,reservation.getVenue())
                    .eq(Reservation::getStatus,"valid");
            List<Reservation> periods = reservationService.list(exist);

            //2. get capacity under this venue
            if(!good.getPeriod().equals("0"))
            {
                LambdaQueryWrapper<Venue> cap = new LambdaQueryWrapper<>();
                cap.eq(Venue::getVid, reservation.getVenue())
                        .eq(Venue::getFid, reservation.getFacility());
                Venue venue = venueService.getOne(cap);
                int capacity = venue.getCapacity();

                //3. statistic the current capacity
                int[] curCap = new int[8];
                for (Reservation res : periods) {
                    String[] per1 = null;
                    int[] per = null;
                    // get the period of this reservation
                    per1 = res.getPeriod().split(",");
                    per = new int[per1.length];
                    for (int i = 0; i < per.length; i++) {
                        per[i] = Integer.parseInt(per1[i]);
                    }
                    // update the current capacity
                    for (int eachPeriod : per) {
                        curCap[eachPeriod - 1] += res.getAmount();
                    }
                }
                // judge whether the capacity is enough
                String[] thisR = reservation.getPeriod().split(",");
                int[] thisRP = new int[thisR.length];
                for (int i = 0; i < thisR.length; i++) {
                    thisRP[i] = Integer.parseInt(thisR[i]);
                }
                for (int thisPeriod : thisRP) {
                    if (thisPeriod > 8 || thisPeriod < 0) {
                        return BackMsg.error("wrong period!");
                    }
                    if (curCap[thisPeriod - 1] + reservation.getAmount() > capacity) {
                        String er = thisPeriod + "has left less than " + reservation.getAmount() +
                                " capacity. Thus, reservation for all periods haven't been reserved successfully!";
                        return BackMsg.error(er);
                    }
                }
            }

            int id = Integer.parseInt(userid);
            reservation.setStatus("unpaid");
            reservation.setRuid(id);
            reservationService.save(reservation);


            String logoPath = "src/main/resources/static/logo/logo.png";
            String destPath = "src/main/resources/static/reservationQR/reservation/"+reservation.getRid()+".jpg";
            QrCodeUtils.encode(reservation.toString(),logoPath,destPath,true);
            good.setReservation(reservation);
        }

        for(Goods goods1: goods)
        {
            Reservation reservation = goods1.getReservation();
            String[] per = reservation.getPeriod().split(",");
            LambdaQueryWrapper<Venue> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(Venue::getVid, reservation.getVenue());
            LambdaQueryWrapper<Facility> queryWrapper1 = new LambdaQueryWrapper<>();
            queryWrapper1.eq(Facility::getFid, reservation.getFacility());
            LambdaQueryWrapper<Account> queryWrapper2 = new LambdaQueryWrapper<>();
            queryWrapper2.eq(Account::getAid, aid);

            LambdaQueryWrapper<Reservation> queryWrapper3 = new LambdaQueryWrapper<>();
            queryWrapper3.eq(Reservation::getRid, reservation.getRid());


            Account account = accountService.getOne(queryWrapper2);
            int pri = venueService.getOne(queryWrapper).getPrice();


            String iden = user.getCustomer().getMembership();
            double discount = discount(iden);
            BigDecimal tot = new BigDecimal(goods1.getPrice()*discount*goods1.getAmount());
            BigDecimal leave = account.getBalance().subtract(tot);

            if (leave.compareTo(new BigDecimal(0)) < 0) {
                return BackMsg.error("account balance not enough!");
            }
            Bill bill = new Bill();
            bill.setFigure(new BigDecimal(goods1.getPrice()));
            bill.setUid(Integer.parseInt(userid));

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            Calendar c = Calendar.getInstance();

            java.util.Date date = c.getTime();
            Date d = new Date(date.getTime());

            String time = df.format(d);

            Timestamp ts = Timestamp.valueOf(time);

            bill.setBdate(ts);
            bill.setVname(venueService.getOne(queryWrapper).getVname());
            bill.setFname(facilityService.getOne(queryWrapper1).getFname());
            bill.setBrid(reservation.getRid());
            bill.setOperator("system");

            billService.save(bill);
            account.setBalance(leave);
            accountService.update(account, queryWrapper2);
            reservation.setStatus("valid");
            reservation.setRuid(Integer.parseInt(userid));
            reservationService.update(reservation, queryWrapper3);
        }
        return BackMsg.success("success");
    }

    public static double discount(String type){
        double fee;
        if (type.equals("copper member")){
            fee = 0.8;
        }
        else if (type.equals("silver member")){
            fee = 0.6;
        }
        else if(type.equals("gold member")){
            fee = 0.3;
        }
        else {
            fee = 1.0;
        }
        return fee;
    }
}
