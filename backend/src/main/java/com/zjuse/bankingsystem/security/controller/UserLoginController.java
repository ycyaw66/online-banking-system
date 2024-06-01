package com.zjuse.bankingsystem.security.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.security.config.JwtConfig;
import com.zjuse.bankingsystem.security.controller.dto.ForgetReq;
import com.zjuse.bankingsystem.security.controller.dto.SendMailReq;
import com.zjuse.bankingsystem.security.controller.dto.UpdateProfileDto;
import com.zjuse.bankingsystem.security.controller.dto.UserLoginReq;
import com.zjuse.bankingsystem.security.controller.dto.UserRegisterReq;
import com.zjuse.bankingsystem.security.security.JwtTokenProvider;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.security.service.EmailValidService;
import com.zjuse.bankingsystem.security.service.OnlineUserService;
import com.zjuse.bankingsystem.security.service.dto.JwtUserDto;
import com.zjuse.bankingsystem.service.UserService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
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
    private EmailValidService emailValidService; 
    @Autowired
    private UserService userService; 
    @Autowired
    private CurrentUserService currentUserService; 
    
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

    @PostMapping("/register/sendMail")
    public RespResult postSendMail(@RequestBody SendMailReq req) {
        ApiResult apiResult = emailValidService.sendEmail(req.getMail(), null);
        if (!apiResult.ok) { 
            return RespResult.fail(apiResult.message);
        }

        String uuid = (String) apiResult.payload; 
        Map<String, String> uuidInfo = new HashMap<String,String>(2) {{
            put("uuid", uuid); 
        }};
        return RespResult.success(uuidInfo);
    }

    @PostMapping("/register")
    public RespResult postUserRegister(@RequestBody UserRegisterReq req) {
        ApiResult apiResult = emailValidService.validCode(req.getUuid(), req.getEmail(), req.getVerificationCode());
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }

        User user = new User(null, req.getUsername(), req.getPassword(), 
            req.getIdNumber(), req.getEmail(), req.getPhoneNumber());
        apiResult = userService.registerNewUser(user);

        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }

        return RespResult.success();
    }

    @PostMapping("/forget")
    public RespResult postForget(@RequestBody ForgetReq req) {
        ApiResult apiResult = emailValidService.validCode(req.getUuid(), req.getEmail(), req.getVerificationCode());
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }

        apiResult = userService.updatePasswordByEmail(req.getEmail(), req.getPassword());
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }

        return RespResult.success();
    }
    
    @PostMapping("/profile/update")
    public RespResult updateProfile(@RequestBody UpdateProfileDto req) {
        ApiResult apiResult = currentUserService.getCurrentUser(); 
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message); 
        }
        User user = (User) apiResult.payload; 
        if (!user.getPassword().equals(req.getPassword())) {
            return RespResult.fail("密码错误");
        }

        apiResult = userService.updateUserByUsername(req.getUsername(), req.getPhoneNumber(), req.getNewPassword());
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }

        return RespResult.success(); 
    }

    @GetMapping("/profile")
    public RespResult getProfile() {
        ApiResult apiResult = currentUserService.getCurrentUser(); 
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message); 
        }
        return RespResult.success(apiResult.payload);
    }

    @DeleteMapping("/logout") 
    public RespResult logout(HttpServletRequest request) {
        String token = jwtTokenProvider.getTokenFromRequest(request);
        onlineUserService.logout(token);
        return RespResult.success();
    }
}
