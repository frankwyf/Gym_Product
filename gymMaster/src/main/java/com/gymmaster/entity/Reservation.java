package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.sql.Date;

@Data
public class Reservation implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer rid;
    /** Customer user ID. Field renamed ruid (was Ruid — PascalCase breaks Lombok getter/column mapping). */
    private int ruid;
    @NotNull(message = "Reservation date is required.")
    private Date rdate;
    @Min(value = 1, message = "Facility ID must be positive.")
    private int facility;
    @Min(value = 1, message = "Venue ID must be positive.")
    private int venue;
    @Min(value = 1, message = "Amount must be at least 1.")
    private int amount;
    @NotBlank(message = "Period is required (comma-separated slot numbers).")
    private String period;
    private String payment;
    private String status;
}
