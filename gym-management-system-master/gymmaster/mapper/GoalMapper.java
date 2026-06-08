package com.gym.gymmaster.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.gymmaster.entity.Goal;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface GoalMapper extends BaseMapper<Goal> {
}
