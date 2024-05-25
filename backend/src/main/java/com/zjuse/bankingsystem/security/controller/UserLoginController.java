package com.zjuse.bankingsystem.security.controller;

import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.security.controller.dto.UserLoginReq;
import com.zjuse.bankingsystem.utils.RespResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserLoginController {
    
    @PostMapping("/login")
    public RespResult postUserLogin(@Validated @RequestBody UserLoginReq req) {
        String username = req.getUsername();
        String password = req.getPassword();
        return RespResult.success();
    }
}
