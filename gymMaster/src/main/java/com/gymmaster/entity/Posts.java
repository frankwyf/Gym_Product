package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Posts implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer pid;
    private int author;
    @Size(max = 50, message = "Post type must be \u226450 characters.")
    private String type;
    @NotBlank(message = "Post content is required.")
    @Size(max = 2000, message = "Post content must be \u22642000 characters.")
    private String content;
    private String media;
    private Timestamp datesent;
}
