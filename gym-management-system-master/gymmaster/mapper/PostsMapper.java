package com.gym.gymmaster.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gym.gymmaster.entity.Posts;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface PostsMapper extends BaseMapper<Posts> {
}
