package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Timestamp;

@Data
public class Course implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer couid;
    @Min(value = 1, message = "Coach ID must be positive.")
    private int coaid;
    @Min(value = 0, message = "Price cannot be negative.")
    private int price;
    @NotBlank(message = "Course type is required.")
    @Size(max = 50, message = "Course type must be \u226450 characters.")
    private String type;
    private String cover;
    @Size(max = 500, message = "Description must be \u2264500 characters.")
    private String description;
    @Min(value = 1, message = "Facility ID must be positive.")
    private int courseFacility;
    private Timestamp time;
    @Min(value = 1, message = "Capability must be at least 1.")
    private int capability;
    @Min(value = 1, message = "Venue ID must be positive.")
    private int courseVenue;
}

