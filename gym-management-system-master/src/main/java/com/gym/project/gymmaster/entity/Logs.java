package com.gym.project.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Logs implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer lid;
    private int uid;
    private String type;
    private String uri;
    private LocalDateTime date;
}
