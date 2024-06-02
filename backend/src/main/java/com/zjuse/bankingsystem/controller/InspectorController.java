package com.zjuse.bankingsystem.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.security.config.JwtConfig;
import com.zjuse.bankingsystem.security.security.JwtTokenProvider;
import com.zjuse.bankingsystem.security.security.enums.LoginType;
import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.service.CreditCardService;
import com.zjuse.bankingsystem.service.InspectorService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/credit-card/inspector")
public class InspectorController {
    @Autowired
    private InspectorService inspectorService;
    @Autowired
    private AuthenticationManager authenticationManager; 
    @Autowired
    private JwtTokenProvider jwtTokenProvider;  
    @Autowired
    private JwtConfig jwtConfig; 

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
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", jwtConfig.getTokenStartWith() + " " + token);
            put("username", jwtUserDto.getUsername());
        }}; 
        return RespResult.success(authInfo);
    }

    @PostMapping("/request")
    public RespResult queryRequestsByInspector(@RequestParam Integer permission) {
        ApiResult apiResult = inspectorService.queryRequestsByInspector(permission);
        return RespResult.success(apiResult.payload);
    }

    @PostMapping("/request/accept")
    public RespResult acceptRequest(@RequestParam Long id) {
        ApiResult apiResult = inspectorService.acceptRequest(id);
        return RespResult.success(apiResult.payload);
    }

    @PostMapping("/request/reject")
    public RespResult rejectRequest(@RequestParam Long id) {
        ApiResult apiResult = inspectorService.rejectRequest(id);
        return RespResult.success(apiResult.payload);
    }
}
