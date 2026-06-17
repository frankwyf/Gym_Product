package com.gym.project.gymmaster.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.project.gymmaster.common.BackMsg;
import com.gym.project.gymmaster.entity.Posts;
import com.gym.project.gymmaster.service.PostsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    PostsService postsService;
    @GetMapping("/init")
    public BackMsg<List<Posts>> initPosts() {
        // load all the posts from the database, length of the array is not fixed
        List<Posts> posts = new ArrayList<>();
        // get all the posts from the database
        LambdaQueryWrapper<Posts> postQueryWrapper = new LambdaQueryWrapper<>();
        // return the posts in the order of the postid
        postQueryWrapper.orderByAsc(Posts::getPid);
        // set the data into arraylist type
        posts = postsService.list(postQueryWrapper);
        log.info("get the slides pictures for the home page");
        return BackMsg.success(posts);
    }

}
