package com.zjuse.bankingsystem.entity.foreignCurrency;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FcAdministrator {
    private String id;
    private String name;
    private String password;
}
