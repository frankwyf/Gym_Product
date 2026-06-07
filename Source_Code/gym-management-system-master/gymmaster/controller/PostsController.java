package com.gym.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.gym.gymmaster.common.BackMsg;
import com.gym.gymmaster.entity.Posts;
import com.gym.gymmaster.service.FacilityService;
import com.gym.gymmaster.service.PostsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@Slf4j
@RestController
@RequestMapping("/posts")
public class PostsController {
    @Autowired
    PostsService postsService;
    @Autowired
    FacilityService facilityService;
    @GetMapping("/init")
    public BackMsg<ArrayList<Posts>> initPosts() {
        // load all the posts from the database, length of the array is not fixed
        ArrayList<Posts> posts = new ArrayList<>();
        // get all the posts from the database
        LambdaQueryWrapper<Posts> postQueryWrapper = new LambdaQueryWrapper();
        // return the posts in the order of the postid
        postQueryWrapper.orderByAsc(Posts::getPid);
        // set the data into arraylist type
        posts = (ArrayList<Posts>) postsService.list(postQueryWrapper);
        log.info("get the slides pictures for the home page");
        return BackMsg.success(posts);
    }

}
