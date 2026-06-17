package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Comments;
import com.gymmaster.entity.LoginUser;
import com.gymmaster.entity.Posts;
import com.gymmaster.service.CommentService;
import com.gymmaster.service.PostsService;
import com.gymmaster.utils.JwtUtil;
import com.gymmaster.utils.RedisCache;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    PostsService postsService;

    @Autowired
    RedisCache redisCache;

    @Autowired
    CommentService commentService;

    @GetMapping("/init")
    public BackMsg<ArrayList<Posts>> initPosts() {
        // get all the posts from the database
        LambdaQueryWrapper<Posts> postQueryWrapper = new LambdaQueryWrapper<>();
        // return the posts in the order of the post id
        postQueryWrapper.orderByAsc(Posts::getPid);
        ArrayList<Posts> posts = new ArrayList<>(postsService.list(postQueryWrapper));
        log.info("get the slides pictures for the home page");
        return BackMsg.success(posts);
    }

    // post a comment for a specific post, return a success message for frontend to show
    @GetMapping(value = "/postComment")
    public BackMsg<String> postComment(HttpServletRequest request) {
        // get current user from redis
        int postID = request.getHeader("PostID") == null ? 0 : Integer.parseInt(request.getHeader("postID"));
        String content = request.getHeader("content");
        String token = request.getHeader("token");
        System.err.println(token);
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            log.error("illegal token in /posts/postComment", e);
            throw  new RuntimeException("illegal token");
        }
        String redisKey = "login"+userid;
        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);
        // get current user id
        int uid = user.getCustomer().getUid();
        // get the current time
        Timestamp timestamp = new Timestamp(new Date().getTime());
        // create a new comment object
        Comments comment = new Comments();
        // set the comment content
        comment.setContent(content);
        // set the comment time
        comment.setDatesent(timestamp);
        // set the post id
        comment.setPid(postID);
        // set the sender id
        comment.setSender(uid);
        // set the sender type
        comment.setSenderType("customer");
        // insert the comment into the database
        commentService.save(comment);
        // return the success message
        return BackMsg.success("success");
    }

    // add a new post
    @GetMapping("/add")
    public BackMsg<String> add(String content, String media, HttpServletRequest request) {
        // get the current user from redis
        String token = request.getHeader("token");
        String userid;
        try {
            Claims claims = JwtUtil.parseJWT(token);
            userid = claims.getSubject();
        } catch (Exception e) {
            log.error("illegal token in /posts/add", e);
            throw  new RuntimeException("illegal token");
        }
        String redisKey = "login"+userid;
        // get information from redis
        LoginUser user = redisCache.getCacheObject(redisKey);
        // get current user id
        int uid = user.getCustomer().getUid();
        // get the current time
        Timestamp timestamp = new Timestamp(new Date().getTime());
        Posts post = new Posts();
        // set the post time
        post.setDatesent(timestamp);
        // set the post sender
        post.setAuthor(uid);
        // set the post sender type
        post.setType("customer");
        // set the post content
        post.setContent(content);
        // set the media of the post
        post.setMedia(media);
        // insert the post into the database
        postsService.save(post);
        // return the success message
        return BackMsg.success("success");
    }
}
