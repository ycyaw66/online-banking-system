package com.zjuse.bankingsystem.entity.foreignCurrency;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Data
@AllArgsConstructor
@TableName("foreign_currency_account")
public class ForeignCurrencyAccount {
    private String credit_card_id;
    private String fc_id;
    private Double amount;
}
