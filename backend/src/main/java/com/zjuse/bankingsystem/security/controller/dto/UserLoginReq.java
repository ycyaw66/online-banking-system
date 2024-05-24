package com.zjuse.bankingsystem.security.controller.dto;

import lombok.Data;

@Data
public class UserLoginReq {
    private String username;
    private String password; 
}
