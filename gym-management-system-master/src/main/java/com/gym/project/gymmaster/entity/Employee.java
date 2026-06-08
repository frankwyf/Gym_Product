package com.gym.project.gymmaster.entity;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Employee implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer eid;
    private String username;
    private String password;
    private String phone;
    private String eMail;
    private String profile;



}
