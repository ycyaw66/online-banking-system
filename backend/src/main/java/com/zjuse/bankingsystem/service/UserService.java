package com.zjuse.bankingsystem.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.protobuf.Api;
import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.mapper.UserMapper;
import com.zjuse.bankingsystem.utils.ApiResult;

@Service
public class UserService {
    @Autowired
    private UserMapper userMapper; 

    public ApiResult registerNewUser(User user) {
        try {
            userMapper.insert(user);
            return new ApiResult(true, null); 
        } catch(Exception e) {
            return new ApiResult(false, e.getMessage()); 
        }
    }

    public ApiResult checkPassword(String username, String password) {
        try {
            User res = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
            if (res == null) 
                return new ApiResult(false, "username not found");
            if (password.equals(res.getPassword()))
                return new ApiResult(true, res);
            else 
                return new ApiResult(false, "password error");
        } catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult getUserById(String id) {
        try {
            User res = userMapper.selectById(id);
            if (res == null)
                return new ApiResult(false, "user id not found");
            return new ApiResult(true, res);
        } catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult getUserByUsername(String username) {
        try {
            User res = userMapper.selectOne(new QueryWrapper<User>().eq("username", username));
            if (Objects.isNull(res))
                return new ApiResult(false, "username not found");
            return new ApiResult(true, res);
        } catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }
}
