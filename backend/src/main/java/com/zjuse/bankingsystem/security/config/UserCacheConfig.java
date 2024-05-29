package com.zjuse.bankingsystem.security.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Data
@Component
@ConfigurationProperties(prefix = "user-cache")
public class UserCacheConfig {
    private Long validityTimeInSeconds; 
    private String userCacheKey; 
}
