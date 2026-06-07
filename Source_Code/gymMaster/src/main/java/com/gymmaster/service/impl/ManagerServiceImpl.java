package com.gymmaster.service.impl;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.gymmaster.entity.Manager;
import com.gymmaster.mapper.ManagerMapper;
import com.gymmaster.service.ManagerService;

import org.springframework.stereotype.Service;

@Service
public class ManagerServiceImpl extends ServiceImpl<ManagerMapper, Manager> implements ManagerService {
}
