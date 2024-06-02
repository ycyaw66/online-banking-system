package com.zjuse.bankingsystem.entity.creditCard;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("credit-card")
public class CreditCard {
    private Long id;
    @JsonProperty("id_number")
    private String idNumber;
    @JsonIgnore
    private String password;
    @JsonProperty("card_limit")
    private BigDecimal cardLimit;
    private BigDecimal loan;
    @JsonProperty("is_lost")
    private Integer isLost;
}
