package com.zjuse.bankingsystem.entity.user;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class HistoryCondition {
    @JsonProperty("card_id")
    Long cardId;
    @JsonProperty("target_card_id")
    Long targetCardId;
    @JsonProperty("least_amount")
    BigDecimal LeastAmount;
    @JsonProperty("most_amount")
    BigDecimal MostAmount;
    @JsonProperty("start_time")
    Long startTime;
    @JsonProperty("end_time")
    Long endTime;
    String remark;
}
