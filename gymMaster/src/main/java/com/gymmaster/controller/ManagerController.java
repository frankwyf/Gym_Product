package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Manager;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.ManagerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/manager")
@RequiredArgsConstructor
public class ManagerController {
    private final ManagerService managerService;

    @Value("${gym.path}")
    private String basePath;

    @PostMapping("/register")
    public BackMsg<String> register(@Valid @RequestBody Manager manager) {
        LambdaQueryWrapper<Manager> qw = new LambdaQueryWrapper<Manager>()
                .eq(Manager::getUsername, manager.getUsername());
        if (managerService.getOne(qw) != null) {
            throw new BusinessException("Username already exists.");
        }
        if (manager.getProfile() == null) {
            manager.setProfile("default.png");
        }
        managerService.save(manager);
        return BackMsg.success("Registered successfully.");
    }

    @GetMapping("/page")
    public BackMsg<Page<Manager>> page(int page, int pageSize, String name) {
        Page<Manager> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Manager> qw = new LambdaQueryWrapper<Manager>()
                .like(StringUtils.isNotEmpty(name), Manager::getUsername, name)
                .orderByDesc(Manager::getMid);
        managerService.page(pageInfo, qw);
        return BackMsg.success(pageInfo);
    }

    @PostMapping("/logout")
    public BackMsg<String> logout(HttpServletRequest request) {
        request.getSession().removeAttribute("manager");
        return BackMsg.success("successfully log out");
    }
}
