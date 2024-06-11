package com.zjuse.bankingsystem.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DataOperatorRegister {

    private String username;
    private String password;
    private String email;
    private String phone;
    private String confirm;
}
