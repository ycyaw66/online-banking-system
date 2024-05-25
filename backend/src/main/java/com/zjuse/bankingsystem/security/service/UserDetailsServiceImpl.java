package com.zjuse.bankingsystem.security.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;

@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    private UserCacheManager userCacheManager; 

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
        return jwtUserDto;
    }
}
