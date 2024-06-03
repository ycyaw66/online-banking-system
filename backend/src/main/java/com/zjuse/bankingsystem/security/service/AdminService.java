package com.zjuse.bankingsystem.security.service;

import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjuse.bankingsystem.entity.Admin;
import com.zjuse.bankingsystem.entity.User;
import com.zjuse.bankingsystem.mapper.AdminMapper;
import com.zjuse.bankingsystem.utils.ApiResult;

@Service
public class AdminService {
    @Autowired
    private AdminMapper adminMapper; 

    public ApiResult getUserByUsername(String username) {
        try {
            Admin res = adminMapper.selectOne(new QueryWrapper<Admin>().eq("username", username));
            if (Objects.isNull(res))
                return new ApiResult(false, "username not found");
            return new ApiResult(true, res);
        } catch(Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }
}
