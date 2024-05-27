package com.zjuse.bankingsystem.security.security;

import java.io.ObjectInputFilter.Config;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

import com.zjuse.bankingsystem.security.config.JwtConfig;

import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {
    @Autowired
    private JwtConfig jwtConfig;
    
    public String createToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return JWT.create()
            .setPayload("username", userDetails.getUsername())
            .setKey(jwtConfig.getTokenSignKey().getBytes(StandardCharsets.UTF_8))
            .sign(); 
    }

    public Authentication getAuthentication(String token) {
        String username = (String)JWTUtil.parseToken(token).getPayload("username");
        return new UsernamePasswordAuthenticationToken(username, "******", new ArrayList<>());
    }

    public String getToken(HttpServletRequest request) {
        final String requestHeader = request.getHeader(jwtConfig.getHeader());
        if (requestHeader != null && requestHeader.startsWith(jwtConfig.getTokenStartWith())) {
            return requestHeader.substring(7);
        }
        return null;
    }

    public String loginKey(String token) {
        String md5Token = DigestUtil.md5Hex(token);
        return jwtConfig.getOnlineKey() + md5Token;
    }
}
