package com.gym.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class Bill implements Serializable{
    @TableId(type = IdType.ASSIGN_UUID)
    private Integer bid;
    private int uid;
    private String fname;
    private String vname;
    private BigDecimal figure;
    private Timestamp bdate;
    private String operator;
    private int brid;
}
