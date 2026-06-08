package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import net.sf.jsqlparser.expression.DateTimeLiteralExpression;

import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Posts implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer pid;
    private int author;
    private String type;
    private String content;
    private String media;
    private Timestamp datesent;

}
