package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;

@Data
public class Facility implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer fid;
    private String recommend;
    @Min(value = 0, message = "Vacancy cannot be negative.")
    private int vacancy;
    @Min(value = 0, message = "Sales cannot be negative.")
    private int sales;
    private Date addDate;
    @NotBlank(message = "Location is required.")
    @Size(max = 200, message = "Location must be \u2264200 characters.")
    private String location;
    @NotBlank(message = "Facility name is required.")
    @Size(max = 100, message = "Facility name must be \u2264100 characters.")
    private String fname;
    @Size(max = 500, message = "Description must be \u2264500 characters.")
    private String description;
    private String profile;
    @Pattern(regexp = "^\\+?[0-9\\-\\s]{7,20}$", message = "Invalid phone number format.")
    private String phone;
}

