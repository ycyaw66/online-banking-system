package com.zjuse.bankingsystem.entity;

import java.math.BigDecimal;
import java.sql.Time;

import org.springframework.web.bind.annotation.RequestMapping;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Data   
@AllArgsConstructor
@NoArgsConstructor
public class History {

    @TableId(type = IdType.AUTO)
    private Long id = 0L;

    @JsonProperty("card_id")
    Long cardId;

    @JsonProperty("target_card")
    Long targetCard;

    BigDecimal amount;

    Long time;
    
    String remark;
}
