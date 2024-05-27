package com.zjuse.bankingsystem.security;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.zjuse.bankingsystem.security.config.JwtConfig;

@SpringBootTest
public class ConfigTest {
    @Autowired
    JwtConfig jwtConfig;

    @Test
    void testJwtConfig() {
        System.out.println(jwtConfig);
    }
}
