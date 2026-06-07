package com.gym.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gym.gymmaster.common.BackMsg;
import com.gym.gymmaster.entity.Coach;
import com.gym.gymmaster.service.CoachService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/coach")
public class CoachController {
    @Autowired
    CoachService coachService;
    @Value("${gym.path}")
    private String basePath;

    @PostMapping("/register")
    public BackMsg<String> register(@RequestBody Coach coach){
        // get all data from the coach table in the database
        LambdaQueryWrapper<Coach> queryWrapper = new LambdaQueryWrapper<>();

        // get username from the database
        queryWrapper.eq(Coach::getUsername,coach.getUsername());

        // get one entry from the database
        Coach coach1 = coachService.getOne(queryWrapper);

        if (coach1 != null){
            return BackMsg.error("Coach name already exists");
        }
        if (coach.getProfile() == null){
            coach.setProfile(basePath+"picture/default.png");
        }
        coachService.save(coach);
        return BackMsg.success("registered successfully");
    }
    @GetMapping("/login")
    public BackMsg<Coach> login(HttpServletRequest request, @RequestBody Coach coach){
        LambdaQueryWrapper<Coach> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Coach::getUsername,coach.getUsername());
        // get one entry from the database
        Coach coach1 = coachService.getOne(queryWrapper);
        if(coach1 == null){
            return BackMsg.error("Login Failed, Wrong username or password");
        }
        if(!coach1.getPassword().equals(coach.getPassword())){
            return BackMsg.error("Login Failed, Wrong username or password");
        }

        request.getSession().setAttribute("coach",coach.getCoaid());

        return BackMsg.success(coach);
    }

    @PostMapping("/add")
    public BackMsg<String> add(@RequestBody Coach coach){
        LambdaQueryWrapper<Coach> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Coach::getUsername,coach.getUsername());
        if(coachService.getOne(queryWrapper)!=null){
            return BackMsg.error("username already existed!");
        }
        if (coach.getProfile() == null){
            coach.setProfile(basePath+"picture/default.png");
        }
        coachService.save(coach);
        return BackMsg.success("new employee added successfully");
    }
    @PostMapping("/logout")
    public BackMsg<String> logout(HttpServletRequest request){
        //clear the session to indicate logout
        request.getSession().removeAttribute("coach");
        return BackMsg.success("successfully log out");
    }
    @GetMapping("/page")
    public BackMsg<Page> page(String page, String pageSize, String name){

        int page1 = Integer.parseInt(page);
        int pageSize1 = Integer.parseInt(pageSize);
        Page pageInfo = new Page(page1,pageSize1);
        LambdaQueryWrapper<Coach> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Coach::getUsername,name);
        queryWrapper.orderByDesc(Coach::getCoaid);
        coachService.page(pageInfo,queryWrapper);
        return BackMsg.success(pageInfo);
    }

    // used to truly update the data in the database
    @PutMapping("/update")
    public BackMsg<String> update(@RequestBody Coach coach){

        LambdaQueryWrapper<Coach> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Coach::getCoaid,coach.getCoaid());
        coachService.update(coach,queryWrapper);
        return BackMsg.success("updated successfully!");
    }
    // used to return all the editable information of the employee
    @GetMapping("/info")
    public BackMsg<Coach> getbyId(@RequestBody Coach coach){
        Coach coach1 = coachService.getById(coach.getCoaid());
        return BackMsg.success(coach1);
    }
    @DeleteMapping
    public BackMsg<String> delet(@RequestBody Coach coach){
        LambdaQueryWrapper<Coach> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Coach::getCoaid,coach.getCoaid());
        coachService.remove(queryWrapper);
        return BackMsg.success("remove the employe successfully");
    }
}

