package com.zjuse.bankingsystem.entity.foreignCurrency;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("trade_record")
public class TradeRecord {
    private String trade_id;
    private String fc_id;
    private LocalDateTime trade_time;
    private Double amount_cny;
    private Double amount_foreign_currency;
    private String user_id;
    private String credit_card_id;
    private Boolean is_buy_in;
}
