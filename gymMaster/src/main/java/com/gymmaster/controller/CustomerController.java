package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.*;
import com.gymmaster.service.AccountService;
import com.gymmaster.service.CustomerService;

import javax.servlet.http.HttpServletRequest;

import com.gymmaster.service.GoalService;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;


@Slf4j
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {
    @Autowired
    RedisCache redisCache;
    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;
    @Autowired
    private GoalService goalService;

    public static java.sql.Date  sqlDateAdd(java.sql.Date date){
        //java.sql.Date currentDate = new Date(System.currentTimeMillis());
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 30);
        java.util.Date utilDate = (java.util.Date) calendar.getTime();
        java.sql.Date d = new Date(utilDate.getTime());
        return d;
    }
    public static BigDecimal fee(String type){
        return switch (type) {
            case "copper member" -> new BigDecimal(10);
            case "silver member" -> new BigDecimal(20);
            default -> new BigDecimal(30);
        };
    }

    private int getCurrentUserId(HttpServletRequest request){
        String token = request.getHeader("token");
        try {
            Claims claims = JwtUtil.parseJWT(token);
            return Integer.parseInt(claims.getSubject());
        } catch (Exception exception) {
            log.warn("Failed to parse customer token", exception);
            throw new RuntimeException("illegal token");
        }
    }

    @GetMapping("/getuid")
    public BackMsg<Integer> getuid(HttpServletRequest request){
        return BackMsg.success(getCurrentUserId(request));
    }
    @PostMapping("/vipMem")
    public BackMsg<String> vipMem(int aid, String type, HttpServletRequest request){
        int userId = getCurrentUserId(request);
        String userid = String.valueOf(userId);
        String redisKey = "login"+userid;
        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);
        Customer customer = user.getCustomer();

        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getUid, customer.getUid());
        LambdaQueryWrapper<Account> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(Account::getAid,aid);
        Account account = accountService.getOne(queryWrapper1);


        java.sql.Date d1 = new java.sql.Date(System.currentTimeMillis());
        if (type.equals(customer.getMembership())){
            if (customer.getExpiredate().after(d1)){
                BigDecimal fee = fee(type);

                BigDecimal rest = account.getBalance().subtract(fee);
                if (rest.compareTo(new BigDecimal(0))<0){
                    return BackMsg.error("balance of this account not enough!");
                }
                customer.setExpiredate(sqlDateAdd(customer.getExpiredate()));
                account.setBalance(rest);
            }
            else { // membership expired, start a new membership
                BigDecimal fee = fee(type);
                BigDecimal rest = account.getBalance().subtract(fee);
                if (rest.compareTo(new BigDecimal(0))<0){
                    return BackMsg.error("balance of this account not enough!");
                }
                customer.setExpiredate(sqlDateAdd(d1));
                account.setBalance(rest);

            }
        }
        else {
            BigDecimal fee = fee(type);
            BigDecimal rest = account.getBalance().subtract(fee);
            if (rest.compareTo(new BigDecimal(0))<0){
                return BackMsg.error("balance of this account not enough!");
            }
            customer.setExpiredate(sqlDateAdd(d1));
            customer.setMembership(type);
            account.setBalance(rest);
        }


        accountService.update(account,queryWrapper1);
        customerService.update(customer, queryWrapper);
        return BackMsg.success("success");
    }


    @PostMapping("/register")
    public BackMsg<String> register(@RequestBody Customer customer){
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getUsername,customer.getUsername());
        Customer customer1 = customerService.getOne(queryWrapper);
        if (customer1 != null){
            return BackMsg.error("The username is existed!");
        }
        if (customer.getProfile() == null){
            customer.setProfile("default.png");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        customer.setPassword(encoder.encode(customer.getPassword()));

        // set the date of register
        // get the current date as a timestamp
         Date registerDate = new Date(System.currentTimeMillis());
        // set the date into the customer object
        customer.setJoindate(registerDate);

        // try to write into database
        try {
            customerService.save(customer);
        } catch (Exception e) {
            log.error("register failed", e);
            return BackMsg.error(e.toString());
        }

        // create goal with the customer id
        Goal goal = new Goal();
        goal.setGoalWeight(0);
        goal.setHeight(0);
        goal.setWeekGoal(0);
        goal.setUid(customer.getUid());
        goal.setWeight(0);
        goal.setTarget("System initial");
        // write into database
        goalService.save(goal);

        return BackMsg.success("Successfully register!");
    }


    // function of customer logout
    @GetMapping("/logout")
    public BackMsg<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("customer");
        return BackMsg.success("Successfully logout!");
    }

    @GetMapping("/CheckInformation")
    public BackMsg<LoginUser> CheckInformation(HttpServletRequest request){
        String userid = String.valueOf(getCurrentUserId(request));
        String redisKey = "login"+userid;

        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);

        return BackMsg.success(user);
    }

    @PostMapping("/update")
    public BackMsg<String> update(@RequestBody Customer customer, HttpServletRequest request){
        // get the customer id
        String userid = String.valueOf(getCurrentUserId(request));
        String redisKey = "login"+userid;
        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);
        customer.setUid(user.getCustomer().getUid());
        customer.setPassword(user.getCustomer().getPassword());
        customer.setJoindate(user.getCustomer().getJoindate());
        customer.setProfile(user.getCustomer().getProfile());
        customer.setExpiredate(user.getCustomer().getExpiredate());

        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getUid,user.getCustomer().getUid());
        user.getCustomer().setUsername(customer.getUsername());
        user.getCustomer().setProfile(customer.getProfile());
        user.getCustomer().setLastName(customer.getLastName());
        user.getCustomer().setFirstName(customer.getFirstName());

        user.getCustomer().setExpiredate(customer.getExpiredate());
        user.getCustomer().setEmail(customer.getEmail());


        user.getCustomer().setGender(customer.getGender());
        redisCache.setCacheObject(redisKey,user);
        customerService.update(customer,queryWrapper);
        return BackMsg.success("updated successfully!");
    }

    // update the customer goal information
    @PostMapping("/updateGoal")
    public BackMsg<String> updateGoal(@RequestBody Goal goal, HttpServletRequest request){
        // get the customer id
        int userId = getCurrentUserId(request);
        String userid = String.valueOf(userId);
        String redisKey = "login"+userid;
        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);

        // get the goal information by the customer id
        LambdaQueryWrapper<Goal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goal::getUid, userId);

        goal.setUid(user.getCustomer().getUid());
        goal.setGid(goalService.getOne(queryWrapper).getGid());

        // update the goal information
        goalService.update(goal,queryWrapper);

        return BackMsg.success("updated successfully!");
    }

    @DeleteMapping
    public BackMsg<String> delete(@RequestBody Customer customer){
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Account> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getUid,customer.getUid());
        queryWrapper1.eq(Account::getUid,customer.getUid());
        accountService.remove(queryWrapper1);
        customerService.remove(queryWrapper);
        return BackMsg.success("remove the customer successfully");
    }

    // get the customer goal information
    @GetMapping("/goal")
    public BackMsg<Goal> getGoal(HttpServletRequest request){
        String userid = String.valueOf(getCurrentUserId(request));
        String redisKey = "login"+userid;

        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);

        // get the goal information by the customer id
        LambdaQueryWrapper<Goal> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Goal::getUid,user.getCustomer().getUid());
        Goal goal = goalService.getOne(queryWrapper);
        return BackMsg.success(goal);
    }

}
