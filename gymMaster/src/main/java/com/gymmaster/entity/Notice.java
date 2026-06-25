package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Notice implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer nid;
    private int publisher;
    private String publisherType;
    @NotBlank(message = "Notice title is required.")
    @Size(max = 50, message = "Title must be ≤50 characters.")
    private String title;
    @NotBlank(message = "Notice content is required.")
    @Size(max = 255, message = "Content must be ≤255 characters.")
    private String content;
    private String noticeMedia;
    private Timestamp noticeDate;
}
