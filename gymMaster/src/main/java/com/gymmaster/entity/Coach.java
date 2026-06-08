package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Coach implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer coaid;
    private String username;
    private String password;
    private String profile;
    private String intro;
    private String firstName;
    private String lastName;
    private String certifications;
    private BigDecimal salary;
}
