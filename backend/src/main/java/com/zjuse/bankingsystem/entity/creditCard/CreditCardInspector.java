package com.zjuse.bankingsystem.entity.creditCard;

import com.baomidou.mybatisplus.annotation.TableName;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("credit_card_inspector")
public class CreditCardInspector {
    private Integer id;
    private String name;
    private String password;
    private Integer permission;
}
