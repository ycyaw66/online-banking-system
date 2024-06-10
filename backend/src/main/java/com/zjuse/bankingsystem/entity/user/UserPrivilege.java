package com.zjuse.bankingsystem.entity.user;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@TableName("user_privilege")
@AllArgsConstructor
public class UserPrivilege {
    @JsonProperty("user_id")
    @NonNull
    Long userId;
    @NonNull
    boolean payment;
    @NonNull
    boolean transfer;
    @NonNull
    boolean receive;
}
