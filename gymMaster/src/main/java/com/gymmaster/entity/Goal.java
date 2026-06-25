package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class Goal implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer gid;
    private int uid;
    @Min(value = 0, message = "Height cannot be negative.")
    private int height;
    @Min(value = 0, message = "Weight cannot be negative.")
    private int weight;
    @Min(value = 0, message = "Goal weight cannot be negative.")
    private int goalWeight;
    @Min(value = 0, message = "Weekly goal cannot be negative.")
    private int weekGoal;
    @Size(max = 100, message = "Target description must be under 100 characters.")
    private String target;
}
