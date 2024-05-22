package com.zjuse.bankingsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("UserPrivilege")
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivilege {
    Long userId;
    boolean transcations;
    boolean transfer;
    boolean loss;
}
