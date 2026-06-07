package com.gym.project.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.project.gymmaster.entity.Logs;
import com.gym.project.gymmaster.mapper.LogMapper;
import com.gym.project.gymmaster.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Logs> implements LogService {
}
