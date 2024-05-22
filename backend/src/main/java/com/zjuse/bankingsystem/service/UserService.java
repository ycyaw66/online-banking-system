package com.zjuse.bankingsystem.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.utils.ApiResult;


@Service
@MapperScan("com.zjuse.bankingsystem.mapper")
public class UserService {
    public ApiResult getUserId(String id_number) {
        return new ApiResult(false, "not implemented");
    }
    
}
