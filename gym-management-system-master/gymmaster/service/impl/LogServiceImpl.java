package com.gym.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.gymmaster.entity.Logs;
import com.gym.gymmaster.mapper.LogMapper;
import com.gym.gymmaster.service.LogService;
import org.springframework.stereotype.Service;

@Service
public class LogServiceImpl extends ServiceImpl<LogMapper, Logs> implements LogService {
}
