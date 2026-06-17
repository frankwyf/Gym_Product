package com.gym.project.gymmaster.controller;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gym.project.gymmaster.common.BackMsg;
import com.gym.project.gymmaster.entity.Logs;
import com.gym.project.gymmaster.service.LogService;

@RequestMapping("/logs")
public class LogsController {
    @Autowired
    LogService logService;
    @PostMapping("/page")
    public BackMsg<Page<Logs>> page(int page, int pageSize, int id){
        Page<Logs> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Logs> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(String.valueOf(id)),Logs::getUid,id);
        queryWrapper.orderByDesc(Logs::getLid);
        logService.page(pageInfo,queryWrapper);
        return BackMsg.success(pageInfo);
    }
}
