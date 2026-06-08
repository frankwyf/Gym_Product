package com.gym.gymmaster.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.gymmaster.entity.Logs;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface LogMapper extends BaseMapper<Logs> {
}
