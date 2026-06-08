package com.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gymmaster.entity.Notice;
import com.gymmaster.mapper.NoticeMapper;
import com.gymmaster.service.NoticeService;

import org.springframework.stereotype.Service;

@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {
}
