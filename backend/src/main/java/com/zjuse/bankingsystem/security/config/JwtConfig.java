package com.zjuse.bankingsystem.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "jwt")
public class JwtConfig {
    private String header;
    private String tokenStartWith; 
    private String tokenValidityInSeconds; 
    private String tokenSignKey; 
    private String onlineKey; 
}
