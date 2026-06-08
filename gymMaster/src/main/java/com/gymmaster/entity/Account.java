package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Account implements Serializable{
    @TableId(type = IdType.AUTO)
    private Integer aid;
    private int uid;
    private BigDecimal balance;
    private String method;
    private Date lastUpdate;
    private boolean isActive;
}
