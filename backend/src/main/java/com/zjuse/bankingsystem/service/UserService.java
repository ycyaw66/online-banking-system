package com.zjuse.bankingsystem.service;

import javax.management.Query;
import org.mybatis.spring.annotation.MapperScan;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.mapper.UserMapper;
import com.zjuse.bankingsystem.utils.ApiResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {
    @Autowired
    private UserMapper userMapper; 
    
    public ApiResult getUserId(String id_number) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("id_number", id_number);
            User user = (User) userMapper.selectOne(wrapper);
            ApiResult result = new ApiResult(true, "success");
            result.payload = user.getId();
            return result;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResult(false, e.getMessage());
        }
    }

    public ApiResult registerNewUser(User user) {
        try {
            userMapper.insert(user);
            return new ApiResult(true, null); 
        } catch(Exception e) {
            return new ApiResult(false, e.getMessage()); 
        }
    }

    public ApiResult updatePasswordByEmail(String email, String newPassword) {
        try {
            UpdateWrapper wrapper = new UpdateWrapper<>();
            wrapper.eq("email", email);
            wrapper.set("password", newPassword);
            userMapper.update(wrapper);
            return new ApiResult(true, "success");
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

    public ApiResult getUserByEmail(String email) {
        try {
            User res = userMapper.selectOne(new QueryWrapper<User>().eq("email", email));
            if (Objects.isNull(res))
                return new ApiResult(false, "email not found");
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
