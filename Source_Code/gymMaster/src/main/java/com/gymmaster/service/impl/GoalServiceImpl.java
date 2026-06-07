package com.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.gymmaster.entity.Goal;
import com.gymmaster.mapper.GoalMapper;
import com.gymmaster.service.GoalService;

import org.springframework.stereotype.Service;

@Service
public class GoalServiceImpl extends ServiceImpl<GoalMapper, Goal> implements GoalService {
}
