package com.zjuse.bankingsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@TableName("blacklist")
@NoArgsConstructor
@AllArgsConstructor
public class Blacklist {
    Long userId;
    String reason;
}
