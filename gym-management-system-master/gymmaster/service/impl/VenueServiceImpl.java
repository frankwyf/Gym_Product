package com.gym.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.gymmaster.entity.Venue;
import com.gym.gymmaster.mapper.VenueMapper;
import com.gym.gymmaster.service.VenueService;
import org.springframework.stereotype.Service;

@Service
public class VenueServiceImpl extends ServiceImpl<VenueMapper, Venue> implements VenueService {
}
