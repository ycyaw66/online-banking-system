package com.zjuse.bankingsystem.security.security;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.zjuse.bankingsystem.security.config.JwtConfig;
import com.zjuse.bankingsystem.utils.RedisUtils;

import cn.hutool.core.date.DateField;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.DigestUtil;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtTokenProvider {
    @Autowired
    private JwtConfig jwtConfig;
    @Autowired
    private RedisUtils redisUtils; 
    
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

    public String getTokenFromRequest(HttpServletRequest request) {
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

    public void checkRenewal(String token) {
        String key = loginKey(token);
        long time = redisUtils.getExpire(key) * 1000; 
        Date expireDate = DateUtil.offset(new Date(), DateField.MILLISECOND, (int) time);
        // 判断当前时间与过期时间的时间差
        long differ = expireDate.getTime() - System.currentTimeMillis();
        // 如果在续期检查的范围内，则续期
        if (differ <= jwtConfig.getDetectTime()) {
            long renew = time + jwtConfig.getRenewTime();
            redisUtils.expire(key, renew, TimeUnit.MILLISECONDS);
        }
    }
}
