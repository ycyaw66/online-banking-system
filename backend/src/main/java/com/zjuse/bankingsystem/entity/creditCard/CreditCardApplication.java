package com.zjuse.bankingsystem.entity.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigInteger;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreditCardApplication {
    private Integer id;
    private String idNumber;
    private BigInteger creditCardId;
    private BigInteger amount;
    private Integer type;
    private Integer status;
    private String password;
}
