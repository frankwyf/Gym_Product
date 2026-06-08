package com.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gymmaster.entity.Course;
import com.gymmaster.service.CourseService;
import com.gymmaster.mapper.CourseMapper;
import org.springframework.stereotype.Service;

@Service
public class CourseServiceImpl extends ServiceImpl<CourseMapper, Course> implements CourseService {
}
