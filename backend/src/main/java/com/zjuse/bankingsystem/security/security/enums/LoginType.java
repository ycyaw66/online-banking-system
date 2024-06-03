package com.zjuse.bankingsystem.security.security.enums;

import lombok.Getter;

public enum LoginType {
    USER("USER"),
    ADMIN("ADMIN"),
    INSPECTOR("INSPECTOR");

    @Getter
    private final String role; 

    LoginType(String role) {
        this.role = role; 
    }
    
}
