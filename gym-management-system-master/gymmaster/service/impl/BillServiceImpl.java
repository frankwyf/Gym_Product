package com.gym.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.gymmaster.entity.Bill;
import com.gym.gymmaster.mapper.BillMapper;
import com.gym.gymmaster.service.BillService;
import org.springframework.stereotype.Service;

@Service
public class BillServiceImpl extends ServiceImpl<BillMapper, Bill> implements BillService {
}
