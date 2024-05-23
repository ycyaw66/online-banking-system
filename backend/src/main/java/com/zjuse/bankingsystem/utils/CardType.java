package com.zjuse.bankingsystem.utils;

import com.baomidou.mybatisplus.annotation.EnumValue;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum CardType { // 信用卡或储蓄卡
    CREDIT_CARD(0), DEBIT_CARD(1);
    
    @EnumValue
    Integer value;
}
