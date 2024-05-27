package com.zjuse.bankingsystem.service;

import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.utils.ApiResult;

@SpringBootTest
@MapperScan("com.zjuse.bankingsystem.mapper")
public class UserServiceTest {
    @Autowired
    private UserService userService; 

    @Test
    public void testUserRegisterService() {
        User newUser = new User(
            null, 
            "fbz",
            // 123456 的 sha256 的值
            "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92", 
            "330127111111111111", 
            "123@qq.com", 
            "15115511551"
        );
        userService.registerNewUser(newUser);
        
    }

    @Test
    public void testUserLoginService() {
        String correctUsername = "zxy"; 
        String correctPassword = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
        String nonExistsUsername = "lxy";
        String wrongPassword = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c93"; 
        ApiResult res; 
        res = userService.checkPassword(correctUsername, correctPassword);
        Assert.isTrue(res.ok, "should login in correctly");
        res = userService.checkPassword(nonExistsUsername, correctPassword);
        Assert.isTrue(res.ok == false, "should not login with non exists username");
        res = userService.checkPassword(correctUsername, wrongPassword);
        Assert.isTrue(res.ok == false, "should not login with wrong password");
    }
}
