package com.gym.project.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class Facility implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer fid;
    private String recommend;
    private int vacancy;
    private int sales;
    private Date addDate;
    private String location;
    private String fname;
    private String description;
    private String profile;
    private String phone;
}

