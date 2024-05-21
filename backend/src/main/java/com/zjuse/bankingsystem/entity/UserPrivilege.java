package com.zjuse.bankingsystem.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserPrivilege {
    Long userId;
    boolean transcations;
    boolean transfer;
    boolean loss;
}
