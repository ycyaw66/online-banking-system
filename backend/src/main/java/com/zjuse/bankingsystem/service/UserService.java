package com.zjuse.bankingsystem.service;

import javax.management.Query;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.mapper.UserMapper;
import com.zjuse.bankingsystem.utils.ApiResult;


@Service
public class UserService {
    @Autowired
    UserMapper userMapper;
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
    
}
