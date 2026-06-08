package com.gym.project.gymmaster.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.sql.Date;

@Data
public class Customer {
    @TableId(type = IdType.AUTO)
    private Integer uid;
    private String username;
    private String password;
    private String profile;
    private String firstName;
    private String lastName;
    private String email;
    private int gender;
    private Date joindate;
    private String membership;
    private Date expiredate;
}
