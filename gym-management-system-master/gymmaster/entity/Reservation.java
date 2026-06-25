package com.gym.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import java.io.Serializable;
import java.sql.Date;


@Data
public class Reservation implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer rid;
    /** Customer user ID. Field renamed ruid (was Ruid — PascalCase breaks Lombok getter/column mapping). */
    private int ruid;
    private Date rdate;
    private int facility;
    private int venue;
    private int amount;
    private String period;
    private String payment;
    private String status;
}
