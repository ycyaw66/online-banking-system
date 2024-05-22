package com.zjuse.bankingsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("cardofperson")
@NoArgsConstructor
@AllArgsConstructor
public class CardOfPerson {
    Long userId;
    Long cardId;
}
