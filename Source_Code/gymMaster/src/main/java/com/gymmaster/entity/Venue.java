package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Venue implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer vid;
    private int fid;
    private String vname;
    private int price;
    private String description;
    private String profile;
    private String status;
    private int capacity;
}
