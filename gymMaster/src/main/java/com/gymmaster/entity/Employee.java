package com.gymmaster.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Data
public class Employee implements Serializable {
    @TableId(type = IdType.AUTO)
    private Integer eid;
    @NotBlank(message = "Username is required.")
    @Size(max = 50, message = "Username must be ≤50 characters.")
    private String username;
    @NotBlank(message = "Password is required.")
    private String password;
    @Pattern(regexp = "^\\+?[0-9\\-]{7,20}$", message = "Invalid phone number format.")
    private String phone;
    @Email(message = "Invalid email address.")
    @Size(max = 100)
    private String eMail;
    private String profile;
}
