package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;

@Data
public class Coach implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer coaid;
    @NotBlank(message = "Username is required.")
    @Size(max = 50, message = "Username must be ≤50 characters.")
    private String username;
    @NotBlank(message = "Password is required.")
    private String password;
    private String profile;
    @Size(max = 255, message = "Intro must be ≤255 characters.")
    private String intro;
    @NotBlank(message = "First name is required.")
    @Size(max = 50)
    private String firstName;
    @NotBlank(message = "Last name is required.")
    @Size(max = 50)
    private String lastName;
    @Size(max = 200, message = "Certifications must be ≤200 characters.")
    private String certifications;
    private BigDecimal salary;
}
