package com.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gymmaster.entity.Coach;
import com.gymmaster.mapper.CoachMapper;
import com.gymmaster.service.CoachService;

import org.springframework.stereotype.Service;

@Service
public class CoachServiceImpl extends ServiceImpl<CoachMapper, Coach> implements CoachService {

}
