package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Account;
import com.gymmaster.entity.LoginUser;
import com.gymmaster.service.AccountService;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/account")
public class AccountController {
    @Autowired
    RedisCache redisCache;
    @Autowired
    private AccountService accountService;
    @GetMapping("/add")
    public BackMsg<Account> add(String method,Float balance,String isActive, HttpServletRequest request) throws ParseException {

        String token = request.getHeader("token");
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            log.error("Failed to parse token in account add", e);
            throw  new RuntimeException("illegal token");
        }
        String redisKey = "login"+userid;

        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);
        Account account = new Account();
        account.setUid(user.getCustomer().getUid());
        account.setBalance(new BigDecimal(balance));
        account.setMethod(method);
        account.setActive(Boolean.parseBoolean(isActive));

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        java.sql.Date d = new java.sql.Date(date.getTime());

        String time= df.format(d);

        Timestamp ts= Timestamp.valueOf(time);
        account.setLastUpdate(ts);
        accountService.save(account);
        return BackMsg.success(account);
    }
    @GetMapping("/edit")
    public BackMsg<String> edit(int aid, int balance){
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getAid,aid);
        Account account = accountService.getOne(queryWrapper);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        java.sql.Date d = new java.sql.Date(date.getTime());

        String time= df.format(d);

        Timestamp ts= Timestamp.valueOf(time);
        account.setLastUpdate(ts);
        BigDecimal remain = account.getBalance();
        BigDecimal charge = new BigDecimal(balance);
        account.setBalance(remain.add(charge));
        // update the account
        accountService.update(account,queryWrapper);

        return BackMsg.success("success");
    }
    @GetMapping("/page")
    public BackMsg<java.util.List<Account>> page(HttpServletRequest request){
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();

        String token = request.getHeader("token");
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            log.error("Failed to parse token in account page", e);
            throw  new RuntimeException("illegal token");
        }
        int uid = Integer.parseInt(userid);
        queryWrapper.eq(Account::getUid, uid);
        return BackMsg.success(accountService.list(queryWrapper));
    }
    @GetMapping("/delete")
    public BackMsg<String> delete(int aid){
        LambdaQueryWrapper<Account> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Account::getAid,aid);
        accountService.remove(queryWrapper);
        return BackMsg.success("success");
    }
}
