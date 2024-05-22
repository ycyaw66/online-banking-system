package com.zjuse.bankingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.service.UserService;
import com.zjuse.bankingsystem.utils.RespResult;


@RestController
@RequestMapping("/user")
public class userLoginController {
    @Autowired
    private UserService userService; 

    @PostMapping("/login")
    public String postMethodName(@RequestBody String entity) {
        //TODO: process POST request
        
        return entity;
    }
    
    @PostMapping("/register")
    public RespResult register(@RequestBody User user) {
        
        return RespResult.success();
    }
}
