package com.gym.project.gymmaster.service.impl;//package com.gymmaster.service.impl;
//
//import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
//import com.gymmaster.entity.LoginUser;
//import com.gymmaster.entity.Customer;
//import com.gymmaster.service.CustomerService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.userdetails.UserDetails;
//import org.springframework.security.core.userdetails.UserDetailsService;
//import org.springframework.security.core.userdetails.UsernameNotFoundException;
//import org.springframework.stereotype.Service;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Objects;
//
//@Service
//public class UserDetailsServiceImpl implements UserDetailsService {
//    @Autowired
//    private CustomerService customerService;
//
//    @Override
//    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//        LambdaQueryWrapper<Customer> queryWrapper = new LambdaQueryWrapper<>();
//        System.out.println(username);
//        queryWrapper.eq(Customer::getUsername,username);
//        Customer customer = customerService.getOne(queryWrapper);
//        if(Objects.isNull(customer)){
//            throw new RuntimeException("wrong username or password");
//
//        }
//        List<String> list = new ArrayList<>(Arrays.asList("pageEmp"));
//        return new LoginUser(customer,list);
//    }
//
//
//}
