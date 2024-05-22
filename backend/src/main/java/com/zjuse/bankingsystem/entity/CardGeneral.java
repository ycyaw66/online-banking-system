package com.zjuse.bankingsystem.entity;

import com.zjuse.bankingsystem.utils.cardType;
import lombok.Data;

@Data
public class CardGeneral {
    Long cardId;
    cardType type;
}