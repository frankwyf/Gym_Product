package com.gym.project.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gym.project.gymmaster.entity.Course;
import com.gym.project.gymmaster.mapper.CourseMapper;
import com.gym.project.gymmaster.service.CourseService;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
}
