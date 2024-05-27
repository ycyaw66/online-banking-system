package com.zjuse.bankingsystem.security.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service("userDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserCacheManager userCacheManager; 
    @Autowired
    private UserService userService; 

    @Override
    public JwtUserDto loadUserByUsername(String username) {
        JwtUserDto jwtUserDto = userCacheManager.getUserCache(username);
        if (Objects.isNull(jwtUserDto)) {
            User user; 
            try {
                user = (User)userService.getUserByUsername(username).payload;
            } catch(Exception e) {
                log.error(e.getMessage(), e);
                return null;
            }
            if (user == null) {

            } else {
                jwtUserDto = new JwtUserDto(
                    user, 
                    // TODO add role
                    null
                );
                userCacheManager.addUserCache(username, jwtUserDto);
            }
        }
        return jwtUserDto;
    }
}
