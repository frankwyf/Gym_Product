package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.common.CurrentUserResolver;
import com.gymmaster.entity.Comments;
import com.gymmaster.entity.Posts;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.CommentService;
import com.gymmaster.service.PostsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/posts")
@RequiredArgsConstructor
public class PostsController {
    private final PostsService postsService;
    private final CommentService commentService;
    private final CurrentUserResolver currentUser;

    @GetMapping("/init")
    public BackMsg<ArrayList<Posts>> initPosts() {
        LambdaQueryWrapper<Posts> qw = new LambdaQueryWrapper<Posts>()
                .orderByAsc(Posts::getPid);
        return BackMsg.success(new ArrayList<>(postsService.list(qw)));
    }

    /** Post a comment on a specific post. */
    @PostMapping("/postComment")
    public BackMsg<String> postComment(@RequestParam int postID,
                                       @RequestParam String content,
                                       HttpServletRequest request) {
        if (postID <= 0) throw new BusinessException("Invalid post ID.");
        if (content == null || content.isBlank()) throw new BusinessException("Comment content cannot be empty.");
        int uid = currentUser.getUserId(request);

        Comments comment = new Comments();
        comment.setContent(content);
        comment.setDatesent(new Timestamp(new Date().getTime()));
        comment.setPid(postID);
        comment.setSender(uid);
        comment.setSenderType("customer");
        commentService.save(comment);
        return BackMsg.success("Comment posted successfully.");
    }

    /** Add a new community post. */
    @PostMapping("/add")
    public BackMsg<String> add(@RequestParam String content,
                               @RequestParam(required = false) String media,
                               HttpServletRequest request) {
        if (content == null || content.isBlank()) throw new BusinessException("Post content cannot be empty.");
        int uid = currentUser.getUserId(request);

        Posts post = new Posts();
        post.setDatesent(new Timestamp(new Date().getTime()));
        post.setAuthor(uid);
        post.setType("customer");
        post.setContent(content);
        post.setMedia(media);
        postsService.save(post);
        return BackMsg.success("Post created successfully.");
    }
}
