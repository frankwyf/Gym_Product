package com.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.gymmaster.entity.Reservation;
import com.gymmaster.mapper.ReservationMapper;
import com.gymmaster.service.ReservationService;

import org.springframework.stereotype.Service;

@Service
public class ReservationServiceImpl extends ServiceImpl<ReservationMapper, Reservation> implements ReservationService {

}
