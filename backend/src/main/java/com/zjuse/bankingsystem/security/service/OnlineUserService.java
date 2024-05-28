package com.zjuse.bankingsystem.security.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.security.config.JwtConfig;
import com.zjuse.bankingsystem.security.security.JwtTokenProvider;
import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.security.service.dto.OnlineUserDto;
import com.zjuse.bankingsystem.utils.RedisUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OnlineUserService {
    @Autowired
    private JwtConfig jwtConfig; 
    @Autowired
    private RedisUtils redisUtils; 
    @Autowired 
    private JwtTokenProvider jwtTokenProvider; 


    /* 
     * 保存在线用户信息
     */
    public void save(JwtUserDto jwtUserDto, String token) {
        OnlineUserDto onlineUserDto = null;
        try {
            onlineUserDto = new OnlineUserDto(jwtUserDto.getUsername(), token, new Date()); 
        } catch(Exception e) {
            log.error(e.getMessage(), e);
        }
        String loginKey = jwtTokenProvider.loginKey(token);
        redisUtils.set(loginKey, onlineUserDto, jwtConfig.getTokenValidityInSeconds());
    }

    /*
     * 在线用户登出
     */
    public void logout(String token) {
        String loginKey = jwtTokenProvider.loginKey(token);
        redisUtils.del(loginKey);
    }

    /*
     * 获得在线用户
     */
    public OnlineUserDto getOnlineUser(String token) {
        String loginKey = jwtTokenProvider.loginKey(token);
        return (OnlineUserDto) redisUtils.get(loginKey);
    }
}
