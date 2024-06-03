package com.zjuse.bankingsystem.entity.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.baomidou.mybatisplus.annotation.TableName;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("credit_card_application")
public class CreditCardApplication {
    private Long id;
    private String idNumber;
    private Long creditCardId;
    private BigDecimal amount;
    private Integer type;
    private Integer status;
    private String password;
}
