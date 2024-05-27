package com.zjuse.bankingsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("cardofperson")
@NoArgsConstructor
@AllArgsConstructor
public class CardOfPerson {
    @JsonProperty("user_id")
    Long userId;
    @JsonProperty("card_id")
    Long cardId;
}
