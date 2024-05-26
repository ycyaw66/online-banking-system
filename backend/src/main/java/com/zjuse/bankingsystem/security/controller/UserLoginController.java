package com.zjuse.bankingsystem.security.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mysql.cj.protocol.AuthenticationProvider;
import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.security.config.JwtConfig;
import com.zjuse.bankingsystem.security.controller.dto.UserLoginReq;
import com.zjuse.bankingsystem.security.security.JwtTokenProvider;
import com.zjuse.bankingsystem.security.service.OnlineUserService;
import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.utils.RespResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserLoginController {
    @Autowired
    private AuthenticationManager authenticationManager; 
    @Autowired
    private JwtTokenProvider jwtTokenProvider; 
    @Autowired
    private JwtConfig jwtConfig; 
    @Autowired
    private OnlineUserService onlineUserService; 
    @Autowired
    private UserDetailsService userDetailsService; 
    
    @PostMapping("/login")
    public RespResult postUserLogin(@Validated @RequestBody UserLoginReq req) {
        String username = req.getUsername();
        String password = req.getPassword();
        
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
            new UsernamePasswordAuthenticationToken(username, password);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch(AuthenticationException e) {
            log.info(e.getMessage(), e);
            return RespResult.fail("账号名或密码错误");
        }


        String token = jwtTokenProvider.createToken(authentication);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", jwtConfig.getTokenStartWith() + " " + token);
            put("user", jwtUserDto.getUser());
        }}; 
        onlineUserService.save(jwtUserDto, token);

        return RespResult.success(authInfo);
    }

    @PostMapping("/register")
    public RespResult postUserRegister() {
        return RespResult.success("success");
    }

    @GetMapping("/profile")
    public RespResult getProfile() {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return RespResult.fail("登陆过期");
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername((String) authentication.getPrincipal());
        if (Objects.isNull(userDetails)) {
            return RespResult.fail("找不到当前信息");
            
        }
        return RespResult.success(userDetails);
    }
}
