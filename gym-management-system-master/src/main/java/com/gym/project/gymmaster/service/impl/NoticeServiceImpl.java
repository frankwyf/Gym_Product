package com.gym.project.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.project.gymmaster.entity.Notice;
import com.gym.project.gymmaster.mapper.NoticeMapper;
import com.gym.project.gymmaster.service.NoticeService;
import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
}
