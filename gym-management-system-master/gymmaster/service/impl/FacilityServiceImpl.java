package com.gym.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.gymmaster.entity.Facility;
import com.gym.gymmaster.mapper.FacilityMapper;
import com.gym.gymmaster.service.FacilityService;
import org.springframework.stereotype.Service;

@Service
public class FacilityServiceImpl extends ServiceImpl<FacilityMapper, Facility> implements FacilityService {
}
