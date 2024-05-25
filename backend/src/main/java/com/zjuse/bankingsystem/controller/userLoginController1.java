package com.zjuse.bankingsystem.controller;

import java.nio.charset.StandardCharsets;
import java.util.Map;

import org.apache.catalina.authenticator.SpnegoAuthenticator.AuthenticateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.security.controller.dto.UserLoginReq;
import com.zjuse.bankingsystem.service.UserService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import cn.hutool.jwt.JWT;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;



@RestController
@RequestMapping("/user1")
public class userLoginController1 {
    @Autowired
    private UserService userService; 
    @Autowired
    private AuthenticationManager authenticationManager; 

    @PostMapping("/login")
    public RespResult postMethodName(@RequestBody UserLoginReq req) {
        String username, password; 
        if (req.getUsername() == null || req.getUsername() == null) 
            return RespResult.fail("request format wrong");
        username = req.getUsername();
        password = req.getPassword();
        ApiResult res = userService.checkPassword(username, password);
        if (res.ok) {
            UsernamePasswordAuthenticationToken authenticationToken = 
                new UsernamePasswordAuthenticationToken(username, password);
            authenticationManager.authenticate(authenticationToken);

            String token = JWT.create()
                .setPayload("username", username)
                // TODO modify jwt key to application.yml
                .setKey("Key".getBytes(StandardCharsets.UTF_8))
                .sign();

            return RespResult.success(res.payload);
        }   else 
            return RespResult.fail(res.message);
    }
    
    @PostMapping("/register")
    public RespResult postRegister(@RequestBody User user) {
        ApiResult res = userService.registerNewUser(user);
        if (res.ok) 
            return RespResult.success(res.payload);
        else 
            return RespResult.fail(res.message);
    }

    @GetMapping("/profile")
    public RespResult getProfile(HttpServletRequest request) {
        String userId = (String) request.getSession().getAttribute("userId");
        ApiResult res = userService.getUserById(userId);
        if (res.ok)
            return RespResult.success(res.payload);
        else 
            return RespResult.fail(res.message);
    }
    
}
