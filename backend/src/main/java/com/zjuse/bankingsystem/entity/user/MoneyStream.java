package com.zjuse.bankingsystem.entity.user;

import java.math.BigDecimal;
import java.sql.Date;
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

@AllArgsConstructor
@NoArgsConstructor
public class MoneyStream {
    BigDecimal income;

    BigDecimal comsume;
}
