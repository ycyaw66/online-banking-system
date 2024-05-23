package com.zjuse.bankingsystem.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("UserPrivilege")
@AllArgsConstructor
public class UserPrivilege {
    @JsonProperty("user_id")
    @NonNull
    Long userId;
    @NonNull
    boolean transcations;
    @NonNull
    boolean transfer;
    @NonNull
    boolean loss;
}
