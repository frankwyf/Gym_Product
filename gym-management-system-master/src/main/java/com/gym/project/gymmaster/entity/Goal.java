package com.gym.project.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;

@Data
public class Goal implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer gid;
    private int uid;
    private int height;
    private int weight;
    private int goalWeight;
    private int weekGoal;
    private String target;
}
