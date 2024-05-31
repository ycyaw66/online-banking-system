package com.zjuse.bankingsystem.security.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class ForgetReq {
    private String email; 
    private String uuid; 
    @JsonProperty("verification_code")
    private String verificationCode; 
    private String password; 
}
