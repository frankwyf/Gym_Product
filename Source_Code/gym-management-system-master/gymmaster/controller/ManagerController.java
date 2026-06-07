package com.gym.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gym.gymmaster.common.BackMsg;
import com.gym.gymmaster.entity.Manager;
import com.gym.gymmaster.service.ManagerService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Slf4j
@RestController
@RequestMapping("/manager")
public class ManagerController {
    @Autowired
    private ManagerService managerService;
    @Value("${gym.path}")
    private String basePath;
    @PostMapping("/register")
    public BackMsg<String> register(@RequestBody Manager manager){
        LambdaQueryWrapper<Manager> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Manager::getUsername,manager.getUsername());
        Manager manager1 = managerService.getOne(queryWrapper);
        if (manager1 != null){
            return BackMsg.error("employee name already exists");
        }
        if (manager.getProfile() == null){
            manager.setProfile(basePath+"picture/default.png");
        }
        managerService.save(manager);
        return BackMsg.success("registered successfully");
    }
    @GetMapping("/login")
    public BackMsg<Manager> login(HttpServletRequest request, @RequestBody Manager manager){
        LambdaQueryWrapper<Manager> queryWrapper = new LambdaQueryWrapper<>();

        queryWrapper.eq(Manager::getUsername,manager.getUsername());
        Manager manager1 = managerService.getOne(queryWrapper);
        if(manager1 == null){
            return BackMsg.error("Login Failed, Wrong username or password");
        }
        if(!manager1.getPassword().equals(manager.getPassword())){
            return BackMsg.error("Login Failed, Wrong username or password");
        }

        request.getSession().setAttribute("manager",manager.getMid());
        return BackMsg.success(manager);
    }
    @GetMapping("/page")
    public BackMsg<Page> page(String page, String pageSize, String name){

        int page1 = Integer.parseInt(page);
        int pageSize1 = Integer.parseInt(pageSize);
        Page pageInfo = new Page(page1,pageSize1);
        LambdaQueryWrapper<Manager> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(name),Manager::getUsername,name);
        queryWrapper.orderByDesc(Manager::getMid);
        managerService.page(pageInfo,queryWrapper);
        return BackMsg.success(pageInfo);
    }
    @PostMapping("/logout")
    public BackMsg<String> logout(HttpServletRequest request){
        //clear the session to indicate logout
        request.getSession().removeAttribute("manager");
        return BackMsg.success("successfully log out");
    }
}
