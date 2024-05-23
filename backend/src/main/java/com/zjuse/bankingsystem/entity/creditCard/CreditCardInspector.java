package com.zjuse.bankingsystem.entity.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardInspector {
    private Integer id;
    private String name;
    private String password;
    private Integer permission;
}
