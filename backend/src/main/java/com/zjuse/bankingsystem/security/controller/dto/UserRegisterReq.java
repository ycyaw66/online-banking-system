package com.zjuse.bankingsystem.security.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class UserRegisterReq {
    private String username;
    private String password;
    @JsonProperty("id_number")
    private String idNumber;
    @JsonProperty("phone_number")
    private String phoneNumber; 
    private String email;
    @JsonProperty("verificaition_code")
    private String verificationCode; 
    private String uuid; 
}
