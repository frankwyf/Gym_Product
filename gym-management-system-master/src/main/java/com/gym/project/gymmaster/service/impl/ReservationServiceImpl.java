package com.gym.project.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.project.gymmaster.entity.Reservation;
import com.gym.project.gymmaster.mapper.ReservationMapper;
import com.gym.project.gymmaster.service.ReservationService;
import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {

}
