package com.zjuse.bankingsystem.entity.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    private BigInteger id;
    private String idNumber;
    private String password;
    private Double cardLimit;
    private Double loan;
    private Integer isLost;
}
