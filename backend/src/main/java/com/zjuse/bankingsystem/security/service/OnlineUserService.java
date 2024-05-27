package com.zjuse.bankingsystem.security.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.security.service.dto.OnlineUserDto;
import com.zjuse.bankingsystem.utils.RedisUtils;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class OnlineUserService {
    @Autowired
    private RedisUtils redisUtils; 


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
    }

    public void logout() {

    }
}
