package com.gym.project.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.project.gymmaster.common.BackMsg;
import com.gym.project.gymmaster.entity.Account;
import com.gym.project.gymmaster.entity.Customer;
import com.gym.project.gymmaster.service.AccountService;
import com.gym.project.gymmaster.service.CustomerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.DigestUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@Slf4j
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    @Autowired
    private CustomerService customerService;
    @Autowired
    private AccountService accountService;
    @Value("${gym.path}")
    private String basePath;

    // function of login by customer, method is get
    @GetMapping("/login")
    public BackMsg<Customer> login(HttpServletRequest request, @RequestBody Customer customer){
        String password = customer.getUsername();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        LambdaQueryWrapper<Account> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getUsername, customer.getUsername());
        Customer cus = customerService.getOne(queryWrapper);
        if (cus == null){
            return BackMsg.error("Username is not existed or password is wrong!");
        }
        else{
            if(!cus.getPassword().equals(password)){
                return BackMsg.error("Username is not existed or password is wrong!");
            }
            queryWrapper1.eq(Account::getUid,cus.getUid());
            Account aco = accountService.getOne(queryWrapper1);
            if (aco == null){
                aco.setUid(cus.getUid());
                aco.setActive(true);
                accountService.save(aco);
            }
            request.getSession().setAttribute("customer",cus.getUid());
            return BackMsg.success(cus);
        }
    }

    @GetMapping("/register")
    public BackMsg<String> register(@RequestBody Customer customer){
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getUsername,customer.getUsername());
        Customer customer1 = customerService.getOne(queryWrapper);
        if (customer1 != null){
            return BackMsg.error("The username is existed!");
        }
        if (customer1.getProfile() == null){
            customer1.setProfile(basePath+"picture/default.png");
        }
        customerService.save(customer1);
        return BackMsg.success("Successfully register!");
    }


    // function of customer logout
    @GetMapping("/logout")
    public BackMsg<String> logout(HttpServletRequest request){
        request.getSession().removeAttribute("customer");
        return BackMsg.success("Successfully logout!");
    }

    @GetMapping("/CheckInformation")
    public BackMsg<Customer> CheckInformation(HttpServletRequest request){
        int uid = (int) request.getSession().getAttribute("customer");  // In login function, set the id of customer
        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>(); // so here get customer id
        queryWrapper.eq(Customer::getUid,uid);
        Customer cus = customerService.getOne(queryWrapper);
        return BackMsg.success(cus);
    }

    @PutMapping("/update")
    public BackMsg<String> update(@RequestBody Customer customer){

        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Customer::getUid,customer.getUid());
        customerService.update(customer,queryWrapper);
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
        return BackMsg.success("remove the employe successfully");
    }
}
