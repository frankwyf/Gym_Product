package com.gym.project.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Connect implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private int student;
    private int course;
}

