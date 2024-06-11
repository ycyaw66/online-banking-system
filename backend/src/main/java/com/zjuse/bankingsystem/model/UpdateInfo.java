package com.zjuse.bankingsystem.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class UpdateInfo {
    public String data_operator_id;
    public String password;
    public String phone_number;
    public String email;
    public String new_password;
}
