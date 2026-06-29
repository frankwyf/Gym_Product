package com.gymmaster.controller;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.common.CurrentUserResolver;
import com.gymmaster.entity.Account;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.AccountService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/account")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class AccountController {
    private final AccountService accountService;
    private final CurrentUserResolver currentUser;

    /** Add a new payment account for the currently logged-in customer. */
    @PostMapping("/add")
    public BackMsg<Account> add(@RequestParam String method,
                                @RequestParam Float balance,
                                @RequestParam boolean isActive,
                                HttpServletRequest request) {
        int uid = currentUser.getUserId(request);
        Account account = new Account();
        account.setUid(uid);
        account.setBalance(new BigDecimal(balance.toString()));
        account.setMethod(method);
        account.setActive(isActive);
        account.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        accountService.save(account);
        return BackMsg.success(account);
    }

    /** Top-up / adjust balance for an existing account. */
    @PutMapping("/edit")
    public BackMsg<String> edit(@RequestParam int aid, @RequestParam int balance) {
        LambdaQueryWrapper<Account> qw = new LambdaQueryWrapper<Account>()
                .eq(Account::getAid, aid);
        Account account = accountService.getOne(qw);
        if (account == null) throw new BusinessException("Account not found.");
        account.setLastUpdate(new Timestamp(System.currentTimeMillis()));
        account.setBalance(account.getBalance().add(new BigDecimal(balance)));
        accountService.update(account, qw);
        return BackMsg.success("Balance updated.");
    }

    /** List all accounts belonging to the authenticated customer. */
    @GetMapping("/page")
    public BackMsg<List<Account>> page(HttpServletRequest request) {
        int uid = currentUser.getUserId(request);
        LambdaQueryWrapper<Account> qw = new LambdaQueryWrapper<Account>()
                .eq(Account::getUid, uid);
        return BackMsg.success(accountService.list(qw));
    }

    @DeleteMapping("/delete")
    public BackMsg<String> delete(@RequestParam int aid) {
        LambdaQueryWrapper<Account> qw = new LambdaQueryWrapper<Account>()
                .eq(Account::getAid, aid);
        accountService.remove(qw);
        return BackMsg.success("Account removed.");
    }
}
