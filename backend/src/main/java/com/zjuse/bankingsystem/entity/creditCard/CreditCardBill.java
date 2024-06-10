package com.zjuse.bankingsystem.entity.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import java.util.PropertyResourceBundle;

import com.baomidou.mybatisplus.annotation.TableName;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("credit_card_bill")
public class CreditCardBill {
    private Long id;
    private String idNumber;
    private BigInteger creditCardId;
    private BigInteger amount;
    private Date billDate;
}
