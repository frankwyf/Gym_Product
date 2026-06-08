package com.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gymmaster.entity.Facility;
import com.gymmaster.mapper.FacilityMapper;
import com.gymmaster.service.FacilityService;
import org.springframework.stereotype.Service;

@Service
public class FacilityServiceImpl extends ServiceImpl<FacilityMapper, Facility> implements FacilityService {
}
