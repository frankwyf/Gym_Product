package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmaster.common.BackMsg;
import com.gymmaster.entity.Coach;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.CoachService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Slf4j
@RestController
@RequestMapping("/coach")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class CoachController {
    private final CoachService coachService;

    @PostMapping("/register")
    public BackMsg<String> register(@Valid @RequestBody Coach coach) {
        LambdaQueryWrapper<Coach> qw = new LambdaQueryWrapper<Coach>()
                .eq(Coach::getUsername, coach.getUsername());
        if (coachService.getOne(qw) != null) {
            throw new BusinessException("Coach username already exists.");
        }
        if (coach.getProfile() == null) {
            coach.setProfile("default.png");
        }
        // Hash the password before saving
        coach.setPassword(new BCryptPasswordEncoder().encode(coach.getPassword()));
        coachService.save(coach);
        return BackMsg.success("Registered successfully.");
    }

    @PostMapping("/add")
    public BackMsg<String> add(@Valid @RequestBody Coach coach) {
        LambdaQueryWrapper<Coach> qw = new LambdaQueryWrapper<Coach>()
                .eq(Coach::getUsername, coach.getUsername());
        if (coachService.getOne(qw) != null) {
            throw new BusinessException("Username already exists.");
        }
        if (coach.getProfile() == null) {
            coach.setProfile("default.png");
        }
        coach.setPassword(new BCryptPasswordEncoder().encode(coach.getPassword()));
        coachService.save(coach);
        return BackMsg.success("New coach added successfully.");
    }

    @GetMapping("/page")
    public BackMsg<Page<Coach>> page(int page, int pageSize, String name) {
        Page<Coach> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Coach> qw = new LambdaQueryWrapper<Coach>()
                .like(StringUtils.isNotEmpty(name), Coach::getUsername, name)
                .orderByDesc(Coach::getCoaid);
        coachService.page(pageInfo, qw);
        return BackMsg.success(pageInfo);
    }

    @PutMapping("/update")
    public BackMsg<String> update(@RequestBody Coach coach) {
        LambdaQueryWrapper<Coach> qw = new LambdaQueryWrapper<Coach>()
                .eq(Coach::getCoaid, coach.getCoaid());
        coachService.update(coach, qw);
        return BackMsg.success("Updated successfully.");
    }

    @GetMapping("/info")
    public BackMsg<Coach> getById(int coachid) {
        Coach coach = coachService.getById(coachid);
        if (coach == null) throw new BusinessException("Coach not found.");
        return BackMsg.success(coach);
    }

    @DeleteMapping
    public BackMsg<String> delete(@RequestBody Coach coach) {
        LambdaQueryWrapper<Coach> qw = new LambdaQueryWrapper<Coach>()
                .eq(Coach::getCoaid, coach.getCoaid());
        coachService.remove(qw);
        return BackMsg.success("Coach removed successfully.");
    }
}

