package com.zjuse.bankingsystem.security.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.utils.RedisUtils;

public class UserCacheManager {
    @Autowired
    private RedisUtils redisUtils; 

    /*
     * 获取用户缓存的数据
     */
    public JwtUserDto getUserCache(String username) {
        if (username != "") {
            Object obj = redisUtils.get(username);
            if (obj != null)
                return (JwtUserDto) obj; 
        }
        return null;
    }
}
