package com.zjuse.bankingsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@TableName("user")
public class User {
    private Long id; 
    private String password;
    private String id_number;
    private String email; 
    private String phone_number;
}
