package com.zjuse.bankingsystem.security.security;

import java.nio.charset.StandardCharsets;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import com.zjuse.bankingsystem.security.config.JwtConfig;

import cn.hutool.jwt.JWT;

@Component
public class JwtTokenProvider {
    @Autowired
    private JwtConfig jwtConfig;
    
    public String createToken(Authentication authentication) {
        return JWT.create()
            .setPayload("authentication", authentication)
            .setKey(jwtConfig.getTokenSignKey().getBytes(StandardCharsets.UTF_8))
            .setSubject(authentication.getName())
            .sign(); 
    }
}
