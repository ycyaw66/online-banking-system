package com.zjuse.bankingsystem.security.controller;

import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.security.config.JwtConfig;
import com.zjuse.bankingsystem.security.security.JwtTokenProvider;
import com.zjuse.bankingsystem.security.security.enums.LoginType;
import com.zjuse.bankingsystem.security.service.OnlineUserService;
import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.service.deposite.CashierService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import cn.hutool.jwt.JWTUtil;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.method.P;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Slf4j
@RestController
@RequestMapping("/counter/cashier")
public class CashierLoginController {
    @Autowired
    private CashierService cashierService; 
    @Autowired 
    private AuthenticationManager authenticationManager; 
    @Autowired
    private JwtTokenProvider jwtTokenProvider; 
    @Autowired
    private JwtConfig jwtConfig; 
    @Autowired 
    private OnlineUserService onlineUserService; 

    @PostMapping("/login")
    public RespResult loginCashier(@RequestParam("username") String username, @RequestParam("password") String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
            new UsernamePasswordAuthenticationToken(LoginType.CASHIER + "-" + username, password);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch(AuthenticationException e) {
            log.info(e.getMessage(), e);
            return RespResult.fail("账号名或密码错误");
        }


        String token = jwtTokenProvider.createToken(authentication, LoginType.CASHIER);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", jwtConfig.getTokenStartWith() + " " + token);
            put("username", jwtUserDto.getUsername());
        }}; 
        onlineUserService.save(jwtUserDto, token);
        return RespResult.success(authInfo);
    }

    @PostMapping("/modify")
    @PreAuthorize("@roleCheck.isRole('CASHIER')")
    public RespResult modifyPassword(@RequestParam("oldpassword") String oldpassword, @RequestParam("newpassword") String newpassword) {
        final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return RespResult.fail("登陆过期"); 
        }
        String username = (String)authentication.getPrincipal();
        ApiResult res = cashierService.getCashierIdByUsername(username.split("-")[1]); 
        if (!res.ok) 
            return RespResult.fail(res.message);
        Long id = (Long) res.payload; 
        ApiResult apiResult1 = cashierService.verifyCashier(id, oldpassword);
        if(!apiResult1.ok)
            return RespResult.fail(apiResult1.message);
        ApiResult apiResult = cashierService.changePassword(id, newpassword);
        if(!apiResult.ok)
            return RespResult.fail(apiResult.message);
        else
            return RespResult.success(apiResult.payload);
    }
}
