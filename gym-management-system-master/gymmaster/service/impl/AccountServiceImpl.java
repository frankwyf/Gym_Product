package com.gym.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.gymmaster.entity.Account;
import com.gym.gymmaster.mapper.AccountMapper;
import com.gym.gymmaster.service.AccountService;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {
}
