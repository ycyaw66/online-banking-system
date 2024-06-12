package com.zjuse.bankingsystem.security.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.entity.foreignCurrency.DataOperator;
import com.zjuse.bankingsystem.model.DataOperatorRegister;
import com.zjuse.bankingsystem.model.LoginInfo;
import com.zjuse.bankingsystem.security.config.JwtConfig;
import com.zjuse.bankingsystem.security.security.JwtTokenProvider;
import com.zjuse.bankingsystem.security.security.enums.LoginType;
import com.zjuse.bankingsystem.security.service.OnlineUserService;
import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.service.foreignCurrency.DataOperatorService;
import com.zjuse.bankingsystem.utils.RespResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/fc/data_operator")
public class DataOperatorLoginController {
    @Autowired
    private DataOperatorService dataOperatorService;
    @Autowired 
    private AuthenticationManager authenticationManager; 
    @Autowired
    private JwtTokenProvider jwtTokenProvider; 
    @Autowired
    private JwtConfig jwtConfig; 
    @Autowired 
    private OnlineUserService onlineUserService; 

    @PostMapping("/start/register")
    public RespResult register(@RequestBody DataOperatorRegister dataOperatorRegister) {
        DataOperator entity = new DataOperator("", dataOperatorRegister.getUsername(), dataOperatorRegister.getPassword(), dataOperatorRegister.getEmail(), dataOperatorRegister.getPhone(), 0, 0, 0);
        if(!dataOperatorRegister.getConfirm().equals("111")){
            return RespResult.fail("邀请码错误");
        }
        try{
            dataOperatorService.register(entity);
        }catch(Exception e){
            return RespResult.fail(e.getMessage());
        }
        return RespResult.success("注册成功");
    }

    @PostMapping("/start/login")
    public RespResult loginDataOperator(@RequestBody LoginInfo loginInfo) {
        String username = loginInfo.getUsername();
        String password = loginInfo.getPassword();
        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = 
            new UsernamePasswordAuthenticationToken(LoginType.OPERATOR + "-" + username, password);
        Authentication authentication;
        try {
            authentication = authenticationManager.authenticate(usernamePasswordAuthenticationToken);
        } catch(AuthenticationException e) {
            log.info(e.getMessage(), e);
            return RespResult.fail("账号名或密码错误");
        }


        String token = jwtTokenProvider.createToken(authentication, LoginType.OPERATOR);
        final JwtUserDto jwtUserDto = (JwtUserDto) authentication.getPrincipal();
        DataOperator dt; 
        try {
            dt = dataOperatorService.selectDataOperatorByUsername(username); 
        } catch(Exception e) {
            return RespResult.fail(e.getMessage());
        }
        Map<String, Object> authInfo = new HashMap<String, Object>(2) {{
            put("token", jwtConfig.getTokenStartWith() + " " + token);
            put("username", jwtUserDto.getUsername());
            put("data_operator_id", dt.getData_operator_id());
        }}; 
        onlineUserService.save(jwtUserDto, token);
        return RespResult.success(authInfo);
    }
}
