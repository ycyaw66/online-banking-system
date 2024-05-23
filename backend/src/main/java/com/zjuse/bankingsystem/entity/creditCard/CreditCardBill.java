package com.zjuse.bankingsystem.entity.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.Date;
import java.util.PropertyResourceBundle;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardBill {
    private Integer id;
    private String idNumber;
    private BigInteger creditCardId;
    private BigInteger amount;
    private Date billDate;
}
