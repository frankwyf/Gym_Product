package com.gym.project.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.project.gymmaster.entity.Bill;
import com.gym.project.gymmaster.mapper.BillMapper;
import com.gym.project.gymmaster.service.BillService;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {
}
