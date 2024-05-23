package com.zjuse.bankingsystem.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.service.UserService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("/user")
public class userLoginController {
    @Autowired
    private UserService userService; 

    @PostMapping("/login")
    public RespResult postMethodName(HttpServletRequest request, @RequestBody Map<String, String> req) {
        String username, password; 
        if (req.get("username") == null || req.get("password") == null) 
            return RespResult.fail("request format wrong");
        username = req.get("username");
        password = req.get("password");
        ApiResult res = userService.checkPassword(username, password);
        if (res.ok) {
            HttpSession session = request.getSession();
            User user = User.class.cast(res.payload);
            session.setAttribute("userId", user.getId());
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
