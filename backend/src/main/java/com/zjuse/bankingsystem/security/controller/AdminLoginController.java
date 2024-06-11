package com.zjuse.bankingsystem.security.controller;

import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.security.config.JwtConfig;
import com.zjuse.bankingsystem.security.controller.dto.UserLoginReq;
import com.zjuse.bankingsystem.security.security.JwtTokenProvider;
import com.zjuse.bankingsystem.security.security.enums.LoginType;
import com.zjuse.bankingsystem.security.service.OnlineUserService;
import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.utils.RespResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@Slf4j
@RestController
@RequestMapping("/admin")
public class AdminLoginController {
    @Autowired
    private AuthenticationManager authenticationManager; 
    @Autowired
    private JwtTokenProvider jwtTokenProvider; 
    @Autowired
    private JwtConfig jwtConfig; 
    @Autowired
    private OnlineUserService onlineUserService; 

    @PostMapping("/login")
    public RespResult postUserLogin(@Validated @RequestBody UserLoginReq req) {
        String username = req.getUsername();
        String password = req.getPassword();
        
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
            new UsernamePasswordAuthenticationToken(LoginType.ADMIN + "-" + username, password);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch(AuthenticationException e) {
            log.info(e.getMessage(), e);
            return RespResult.fail("账号名或密码错误");
        }


        String token = jwtTokenProvider.createToken(authentication, LoginType.ADMIN);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", jwtConfig.getTokenStartWith() + " " + token);
            put("username", jwtUserDto.getUsername());
        }}; 
        onlineUserService.save(jwtUserDto, token);

        return RespResult.success(authInfo);
    }

    @DeleteMapping("/logout") 
    public RespResult logout(HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        onlineUserService.logout(token);
        return RespResult.success();
    }
}
