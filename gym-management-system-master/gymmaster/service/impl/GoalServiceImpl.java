package com.gym.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.gymmaster.entity.Goal;
import com.gym.gymmaster.mapper.GoalMapper;
import com.gym.gymmaster.service.GoalService;
import org.springframework.stereotype.Service;

@Service
public class GoalServiceImpl extends ServiceImpl<GoalMapper, Goal> implements GoalService {
}
