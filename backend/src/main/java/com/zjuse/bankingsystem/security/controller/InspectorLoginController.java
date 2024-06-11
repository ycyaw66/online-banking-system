package com.zjuse.bankingsystem.security.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.entity.creditCard.CreditCardInspector;
import com.zjuse.bankingsystem.security.config.JwtConfig;
import com.zjuse.bankingsystem.security.security.JwtTokenProvider;
import com.zjuse.bankingsystem.security.security.enums.LoginType;
import com.zjuse.bankingsystem.security.service.OnlineUserService;
import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.service.creditCard.InspectorService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/credit-card/inspector")
public class InspectorLoginController {
    @Autowired
    private InspectorService inspectorService; 
    @Autowired 
    private AuthenticationManager authenticationManager; 
    @Autowired
    private JwtTokenProvider jwtTokenProvider; 
    @Autowired
    private JwtConfig jwtConfig; 
    @Autowired 
    private OnlineUserService onlineUserService; 

    @PostMapping("/login")
    public RespResult loginInspector(@RequestParam String name, @RequestParam String password) {
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
            new UsernamePasswordAuthenticationToken(LoginType.INSPECTOR + "-" + name, password);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch(AuthenticationException e) {
            log.info(e.getMessage(), e);
            return RespResult.fail("账号名或密码错误");
        }


        String token = jwtTokenProvider.createToken(authentication, LoginType.INSPECTOR);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        ApiResult apiResult = inspectorService.getInspectorByUsername(name);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        CreditCardInspector inspector = (CreditCardInspector) apiResult.payload; 
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", jwtConfig.getTokenStartWith() + " " + token);
            put("username", jwtUserDto.getUsername());
            put("permission", inspector.getPermission()); 
        }}; 
        onlineUserService.save(jwtUserDto, token);
        return RespResult.success(authInfo);
    }
}
