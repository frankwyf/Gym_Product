package com.gym.project.gymmaster.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gym.project.gymmaster.common.BackMsg;
import com.gym.project.gymmaster.entity.Bill;
import com.gym.project.gymmaster.service.BillService;

@RestController
@RequestMapping("/bill")
@SuppressWarnings("null")
public class BillController {
    /* implement the following interface */
    @Autowired
    BillService billService;
    //FacilityService
    //按照种类查看流水，按照月份、周查看流水，
    @GetMapping("/page/period")
    public BackMsg<Map<String, BigDecimal>> pagePeriod(Timestamp start, Timestamp endTime) {
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

        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
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
    public BackMsg<Page<Bill>> page(int page, int pageSize, String name){

        Page<Bill> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Bill> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Bill::getFname,name);
        queryWrapper.orderByDesc(Bill::getBid);
        billService.page(pageInfo,queryWrapper);
        return BackMsg.success(pageInfo);
    }

    @PostMapping("/add")
    public void add(@RequestBody Bill bill){
        billService.save(bill);
    }
}
