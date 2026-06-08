package com.gymmaster.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gymmaster.entity.Comments;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface CommentMapper extends BaseMapper<Comments> {
}
