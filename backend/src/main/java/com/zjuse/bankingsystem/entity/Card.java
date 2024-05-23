package com.zjuse.bankingsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.zjuse.bankingsystem.utils.CardType;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("card")
@AllArgsConstructor
public class Card {
    @JsonProperty("card_id")
    Long cardId;
    @NonNull
    CardType cardType;
}
