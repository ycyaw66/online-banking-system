package com.zjuse.bankingsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@TableName("blacklist")
@AllArgsConstructor
public class Blacklist {
    @JsonProperty("user_id")
    @NonNull
    Long userId;
    @NonNull
    String reason;
}
