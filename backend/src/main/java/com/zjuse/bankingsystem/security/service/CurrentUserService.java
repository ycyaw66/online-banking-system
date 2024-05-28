package com.zjuse.bankingsystem.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.service.UserService;
import com.zjuse.bankingsystem.utils.ApiResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrentUserService {
    @Autowired
    private UserService userService; 

    public ApiResult getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return new ApiResult(false, "登陆过期");
        }
        ApiResult apiResult = userService.getUserByUsername((String) authentication.getPrincipal());
        return apiResult; 
    }

    public ApiResult getCurrentUserIdNumber() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return new ApiResult(false, "登陆过期");
        }
        ApiResult apiResult = userService.getUserByUsername((String) authentication.getPrincipal());
        if (!apiResult.ok) {
            return new ApiResult(false, "登录状态错误");
        }
        User user = (User) apiResult.payload;
        return new ApiResult(true, "", user.getIdNumber()); 
    }
}
 