package com.gym.project.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.project.gymmaster.entity.Coach;
import com.gym.project.gymmaster.mapper.CoachMapper;
import com.gym.project.gymmaster.service.CoachService;
import org.springframework.stereotype.Service;

@Service
public class CoachServiceImpl extends ServiceImpl<CoachMapper, Coach> implements CoachService {

}
