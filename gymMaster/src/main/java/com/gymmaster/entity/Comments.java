package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Comments implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer commentid;
    private int pid;
    private int sender;
    private String senderType;
    private String content;
    private Timestamp datesent;
}
