package com.gymmaster.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gymmaster.entity.Comments;
import com.gymmaster.mapper.CommentMapper;
import com.gymmaster.service.CommentService;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl extends ServiceImpl<CommentMapper, Comments> implements CommentService {
}
