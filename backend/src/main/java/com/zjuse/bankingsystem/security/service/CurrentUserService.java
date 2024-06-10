package com.zjuse.bankingsystem.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.entity.user.User;
import com.zjuse.bankingsystem.service.deposite.CashierService;
import com.zjuse.bankingsystem.service.user.UserService;
import com.zjuse.bankingsystem.utils.ApiResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class CurrentUserService {
    @Autowired
    private UserService userService; 
    @Autowired
    private CashierService cashierService; 

    public ApiResult getCurrentCashierId() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return new ApiResult(false, "登陆过期");
        }
        String username = (String) authentication.getPrincipal(); 
        ApiResult apiResult = cashierService.getCashierIdByUsername(username.split("-")[1]);
        return apiResult; 
    }

    public ApiResult getCurrentUser() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return new ApiResult(false, "登陆过期");
        }
        String username = (String) authentication.getPrincipal(); 
        ApiResult apiResult = userService.getUserByUsername(username.split("-")[1]);
        return apiResult; 
    }

    public ApiResult getCurrentUsername() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return new ApiResult(false, "登陆过期");
        }
        String username = (String) authentication.getPrincipal(); 
        ApiResult apiResult = userService.getUserByUsername(username.split("-")[1]);
        if (!apiResult.ok) {
            return new ApiResult(false, "登录状态错误");
        }
        User user = (User) apiResult.payload;
        return new ApiResult(true, "", user.getUsername()); 
    }

    public ApiResult getCurrentUserIdNumber() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return new ApiResult(false, "登陆过期");
        }
        String username = (String) authentication.getPrincipal(); 
        ApiResult apiResult = userService.getUserByUsername(username.split("-")[1]);
        if (!apiResult.ok) {
            return new ApiResult(false, "登录状态错误");
        }
        User user = (User) apiResult.payload;
        return new ApiResult(true, "", user.getIdNumber()); 
    }
}
 