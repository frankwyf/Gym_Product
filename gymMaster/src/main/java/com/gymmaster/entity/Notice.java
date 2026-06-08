package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;


import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Notice implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer nid;
    private int publisher;
    private String publisherType;
    private String title;
    private String content;
    private String noticeMedia;
    private Timestamp noticeDate;

}
