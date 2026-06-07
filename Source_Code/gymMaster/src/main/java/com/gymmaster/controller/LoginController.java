package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Customer;
import com.gymmaster.entity.LoginUser;
import com.gymmaster.service.CustomerService;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/loginCus")
public class LoginController {
    @Autowired
    CustomerService customerService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    RedisCache redisCache;
    @PostMapping("/login")
    public BackMsg login(@RequestBody Customer customer){
        // get authenticationManager
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(customer.getUsername(), customer.getPassword());

        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        if(Objects.isNull(authenticate)){
            return BackMsg.error("login failed");
        }
        // authentication success
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        int id = loginUser.getCustomer().getUid();
        String uid = Integer.toString(id);
        String jwt = JwtUtil.createJWT(uid);

        //save into redis
        Map<String,String> map = new HashMap<>();
        map.put("token",jwt);
        redisCache.setCacheObject("login"+uid,loginUser,30, TimeUnit.MINUTES);

        return BackMsg.success(map);
    }

    @GetMapping("/logout")
    public BackMsg logout(){
        //获取对应userid
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UsernamePasswordAuthenticationToken authenticationToken = (UsernamePasswordAuthenticationToken) authentication;
        LoginUser customer = (LoginUser) authenticationToken.getPrincipal();
        int id = customer.getCustomer().getUid();

        //delete redis cache
        redisCache.deleteObject("login"+id);
        return BackMsg.success("logout successfully");
    }

    // get all the emails of customers in the database
    @GetMapping("/getEmails")
    public ArrayList<String> getEmails(){
        // get all the customers from the database
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        // for each customer, get the email and add it to the arraylist
        ArrayList<String> emails = new ArrayList<>();
        for (Customer customer : customerService.list(queryWrapper)){
            // if there is no repeated email, add it to the arraylist
            if (!emails.contains(customer.getEmail()))
                emails.add(customer.getEmail());
        }
        return emails;
    }

    // reset password for the customer by username
    @GetMapping("/resetPassword")
    public BackMsg resetPassword(String name, String newPassword){
        // get the customer by username
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getUsername, name);
        Customer customer = customerService.getOne(queryWrapper);
        // if the customer does not exist, return error message
        if (customer == null)
            return BackMsg.error("The customer does not exist!");
        // if the customer exists, reset the password
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        customer.setPassword(encoder.encode(newPassword));
        customerService.update(customer, queryWrapper);
        return BackMsg.success("Reset password successfully!");
    }
}
