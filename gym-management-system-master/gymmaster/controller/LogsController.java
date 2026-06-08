package com.gym.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gym.gymmaster.common.BackMsg;
import com.gym.gymmaster.entity.Logs;
import com.gym.gymmaster.service.LogService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/logs")
public class LogsController {
    @Autowired
    LogService logService;
    @PostMapping("/page")
    public BackMsg<Page> page(int page, int pageSize, int id){
        Page pageInfo = new Page(page,pageSize);
        LambdaQueryWrapper<Logs> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.like(StringUtils.isNotEmpty(String.valueOf(id)),Logs::getUid,id);
        queryWrapper.orderByDesc(Logs::getLid);
        logService.page(pageInfo,queryWrapper);
        return BackMsg.success(pageInfo);
    }
}
