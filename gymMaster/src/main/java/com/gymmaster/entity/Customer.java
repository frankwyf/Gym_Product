package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.sql.Date;

@Data
public class Customer {
    @TableId(type = IdType.AUTO)
    private Integer uid;

    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be 3–50 characters")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private String profile;

    private String firstName;
    private String lastName;

    @Email(message = "Must be a valid email address")
    private String email;

    private int gender;
    private Date joindate;
    private String membership;
    private Date expiredate;
}
