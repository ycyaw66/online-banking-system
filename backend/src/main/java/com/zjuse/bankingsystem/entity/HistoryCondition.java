package com.zjuse.bankingsystem.entity;

import java.math.BigDecimal;

import lombok.Data;

@Data
public class HistoryCondition {
    Long cardId;
    Long targetCardId;
    BigDecimal LeastAmount;
    BigDecimal MostAmount;
    Long startTime;
    Long endTime;
    String remark;
}
