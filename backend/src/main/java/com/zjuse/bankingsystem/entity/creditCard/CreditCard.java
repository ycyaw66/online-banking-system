package com.zjuse.bankingsystem.entity.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCard {
    private Long id;
    private String idNumber;
    private String password;
    private BigDecimal cardLimit;
    private BigDecimal loan;
    private Integer isLost;
}
