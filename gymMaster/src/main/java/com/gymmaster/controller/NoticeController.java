package com.gymmaster.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmaster.common.BackMsg;
import com.gymmaster.common.ThreadContext;
import com.gymmaster.entity.Notice;
import com.gymmaster.service.NoticeService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    @PostMapping("/add")
    public BackMsg<String> add(@RequestBody Notice notice){
        if(!Objects.equals(ThreadContext.getCurrentType(), "manager") &&
                !Objects.equals(ThreadContext.getCurrentType(), "employee")){
            return BackMsg.error("you don't have the permission to post a notice!");
        }
        if (notice.getContent().length()>255 || notice.getTitle().length()>50){
            return BackMsg.error("texts too long");
        }
        notice.setPublisherType(ThreadContext.getCurrentType());
        notice.setPublisher(ThreadContext.getCurrentId());
        Timestamp d = new Timestamp(System.currentTimeMillis());
        notice.setNoticeDate(d);

        noticeService.save(notice);
        return BackMsg.success("registered successfully");
    }
    @DeleteMapping
    public BackMsg<String> delet(@RequestBody Notice notice){
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Notice::getNid,notice.getNid());
        noticeService.remove(queryWrapper);
        return BackMsg.success("remove the employe successfully");
    }
    @GetMapping("/page")
    public BackMsg<Page<Notice>> page(int page, int pageSize, String name){

        Page<Notice> pageInfo = new Page<>(page,pageSize);
        LambdaQueryWrapper<Notice> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(StringUtils.isNotEmpty(name),Notice::getTitle,name);
        queryWrapper.orderByDesc(Notice::getNid);
        noticeService.page(pageInfo,queryWrapper);
        return BackMsg.success(pageInfo);
    }

}
