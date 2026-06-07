package com.gym.project.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Manager implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer mid;
    private String username;
    private String password;
    private String phone;
    private String email;
    private String profile;
}
