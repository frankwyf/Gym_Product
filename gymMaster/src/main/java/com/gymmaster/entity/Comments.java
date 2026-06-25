package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Comments implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer commentid;
    private int pid;
    private int sender;
    @Size(max = 20, message = "Sender type must be \u226420 characters.")
    private String senderType;
    @NotBlank(message = "Comment content is required.")
    @Size(max = 1000, message = "Comment must be \u22641000 characters.")
    private String content;
    private Timestamp datesent;
}
