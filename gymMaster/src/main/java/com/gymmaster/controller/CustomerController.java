package com.gymmaster.controller;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.common.CurrentUserResolver;
import com.gymmaster.entity.Account;
import com.gymmaster.entity.Customer;
import com.gymmaster.entity.Goal;
import com.gymmaster.entity.LoginUser;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.AccountService;
import com.gymmaster.service.CustomerService;
import com.gymmaster.service.GoalService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/customer")
public class CustomerController {

    private final CurrentUserResolver currentUser;
    private final CustomerService customerService;
    private final AccountService accountService;
    private final GoalService goalService;

    private static Date sqlDateAdd(Date date) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, 30);
        return new Date(calendar.getTime().getTime());
    }

    private static BigDecimal membershipFee(String type) {
        return switch (type) {
            case "copper member" -> new BigDecimal("10.00");
            case "silver member" -> new BigDecimal("20.00");
            default -> new BigDecimal("30.00");
        };
    }

    @GetMapping("/getuid")
    public BackMsg<Integer> getuid(HttpServletRequest request) {
        return BackMsg.success(currentUser.getUserId(request));
    }

    @PostMapping("/vipMem")
    public BackMsg<String> vipMem(int aid, String type, HttpServletRequest request) {
        LoginUser loginUser = currentUser.getLoginUser(request);
        Customer customer = loginUser.getCustomer();

        LambdaQueryWrapper<Account> accountQuery = new LambdaQueryWrapper<Account>()
                .eq(Account::getAid, aid);
        Account account = accountService.getOne(accountQuery);
        if (account == null) {
            throw new BusinessException("Account not found.");
        }

        BigDecimal fee = membershipFee(type);
        BigDecimal balance = account.getBalance().subtract(fee);
        if (balance.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("Insufficient account balance.");
        }

        Date today = new Date(System.currentTimeMillis());
        if (type.equals(customer.getMembership()) && customer.getExpiredate() != null && customer.getExpiredate().after(today)) {
            customer.setExpiredate(sqlDateAdd(customer.getExpiredate()));
        } else {
            customer.setExpiredate(sqlDateAdd(today));
            customer.setMembership(type);
        }
        account.setBalance(balance);

        LambdaQueryWrapper<Customer> customerQuery = new LambdaQueryWrapper<Customer>()
                .eq(Customer::getUid, customer.getUid());
        accountService.update(account, accountQuery);
        customerService.update(customer, customerQuery);
        return BackMsg.success("Membership updated successfully.");
    }

    @PostMapping("/register")
    public BackMsg<String> register(@Valid @RequestBody Customer customer) {
        LambdaQueryWrapper<Customer> query = new LambdaQueryWrapper<Customer>()
                .eq(Customer::getUsername, customer.getUsername());
        if (customerService.getOne(query) != null) {
            throw new BusinessException("Username already taken.");
        }

        if (customer.getProfile() == null) {
            customer.setProfile("default.png");
        }
        customer.setPassword(new BCryptPasswordEncoder().encode(customer.getPassword()));
        customer.setJoindate(new Date(System.currentTimeMillis()));
        customerService.save(customer);

        Goal goal = new Goal();
        goal.setGoalWeight(0);
        goal.setHeight(0);
        goal.setWeekGoal(0);
        goal.setUid(customer.getUid());
        goal.setWeight(0);
        goal.setTarget("System initial");
        goalService.save(goal);

        return BackMsg.success("Registration successful!");
    }

    @GetMapping("/CheckInformation")
    public BackMsg<LoginUser> checkInformation(HttpServletRequest request) {
        return BackMsg.success(currentUser.getLoginUser(request));
    }

    @GetMapping("/logout")
    public BackMsg<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("customer");
        return BackMsg.success("Successfully logout!");
    }

    @PostMapping("/update")
    public BackMsg<String> update(@RequestBody Customer customer, HttpServletRequest request) {
        LoginUser loginUser = currentUser.getLoginUser(request);
        Customer existing = loginUser.getCustomer();

        customer.setUid(existing.getUid());
        customer.setPassword(existing.getPassword());
        customer.setJoindate(existing.getJoindate());
        customer.setProfile(existing.getProfile());
        customer.setExpiredate(existing.getExpiredate());

        LambdaQueryWrapper<Customer> query = new LambdaQueryWrapper<Customer>()
                .eq(Customer::getUid, existing.getUid());
        customerService.update(customer, query);

        existing.setUsername(customer.getUsername());
        existing.setFirstName(customer.getFirstName());
        existing.setLastName(customer.getLastName());
        existing.setEmail(customer.getEmail());
        existing.setGender(customer.getGender());
        return BackMsg.success("Profile updated successfully.");
    }

    @PostMapping("/updateGoal")
    public BackMsg<String> updateGoal(@RequestBody Goal goal, HttpServletRequest request) {
        int userId = currentUser.getUserId(request);
        LambdaQueryWrapper<Goal> query = new LambdaQueryWrapper<Goal>()
                .eq(Goal::getUid, userId);
        Goal existing = goalService.getOne(query);
        if (existing == null) {
            throw new BusinessException("Goal record not found for user " + userId);
        }
        goal.setUid(userId);
        goal.setGid(existing.getGid());
        goalService.update(goal, query);
        return BackMsg.success("Goal updated successfully.");
    }

    @DeleteMapping
    public BackMsg<String> delete(@RequestBody Customer customer) {
        LambdaQueryWrapper<Customer> customerQuery = new LambdaQueryWrapper<Customer>()
                .eq(Customer::getUid, customer.getUid());
        LambdaQueryWrapper<Account> accountQuery = new LambdaQueryWrapper<Account>()
                .eq(Account::getUid, customer.getUid());
        accountService.remove(accountQuery);
        customerService.remove(customerQuery);
        return BackMsg.success("Customer removed successfully.");
    }

    @GetMapping("/goal")
    public BackMsg<Goal> getGoal(HttpServletRequest request) {
        int userId = currentUser.getUserId(request);
        LambdaQueryWrapper<Goal> query = new LambdaQueryWrapper<Goal>()
                .eq(Goal::getUid, userId);
        return BackMsg.success(goalService.getOne(query));
    }
}