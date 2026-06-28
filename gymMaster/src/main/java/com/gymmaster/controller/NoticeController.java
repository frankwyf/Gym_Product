package com.gymmaster.controller;

import java.sql.Timestamp;
import java.util.Objects;

import org.apache.commons.lang.StringUtils;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.gymmaster.common.BackMsg;
import com.gymmaster.common.ThreadContext;
import com.gymmaster.entity.Notice;
import com.gymmaster.exception.BusinessException;
import com.gymmaster.service.NoticeService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/notice")
@RequiredArgsConstructor
public class NoticeController {
    private final NoticeService noticeService;

    @CacheEvict(value = "notices", allEntries = true)
    @PostMapping("/add")
    public BackMsg<String> add(@RequestBody Notice notice) {
        String currentType = ThreadContext.getCurrentType();
        if (!Objects.equals(currentType, "manager") && !Objects.equals(currentType, "employee")) {
            throw new BusinessException("Insufficient permissions to post a notice.");
        }
        if (notice.getContent() == null || notice.getContent().length() > 255) {
            throw new BusinessException("Notice content must be 1–255 characters.");
        }
        if (notice.getTitle() == null || notice.getTitle().length() > 50) {
            throw new BusinessException("Notice title must be 1–50 characters.");
        }
        notice.setPublisherType(currentType);
        notice.setPublisher(ThreadContext.getCurrentId());
        notice.setNoticeDate(new Timestamp(System.currentTimeMillis()));
        noticeService.save(notice);
        return BackMsg.success("Notice published.");
    }

    @CacheEvict(value = "notices", allEntries = true)
    @DeleteMapping
    public BackMsg<String> delete(@RequestBody Notice notice) {
        LambdaQueryWrapper<Notice> qw = new LambdaQueryWrapper<Notice>()
                .eq(Notice::getNid, notice.getNid());
        noticeService.remove(qw);
        return BackMsg.success("Notice removed.");
    }

    @GetMapping("/page")
    public BackMsg<Page<Notice>> page(int page, int pageSize, String name) {
        Page<Notice> pageInfo = new Page<>(page, pageSize);
        LambdaQueryWrapper<Notice> qw = new LambdaQueryWrapper<Notice>()
                .like(StringUtils.isNotEmpty(name), Notice::getTitle, name)
                .orderByDesc(Notice::getNid);
        noticeService.page(pageInfo, qw);
        return BackMsg.success(pageInfo);
    }
}
