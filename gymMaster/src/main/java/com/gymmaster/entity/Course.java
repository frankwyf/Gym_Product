package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.sql.Timestamp;
import java.time.DateTimeException;

@Data
public class Course implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer couid;
    private int coaid;
    private int price;
    private String type;
    private String cover;
    private String description;
    private int courseFacility;
    private Timestamp time;
    private int capability;
    private int courseVenue;
}

