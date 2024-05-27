package com.zjuse.bankingsystem.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.utils.RedisUtils;

import cn.hutool.core.util.RandomUtil;

@Component
public class UserCacheManager {
    @Autowired
    private RedisUtils redisUtils; 
    private long cacheTime = 7200000;

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

    /*
     * 添加用户数据到缓存
     */
    public void addUserCache(String username, JwtUserDto jwtUserDto) {
        if (username != null) {
            long time = cacheTime + RandomUtil.randomInt(999, 1999);
            redisUtils.set(username, jwtUserDto, time);
        }
    } 
}
