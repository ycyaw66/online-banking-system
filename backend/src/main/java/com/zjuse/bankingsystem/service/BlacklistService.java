package com.zjuse.bankingsystem.service;

import java.util.List;

import javax.management.Query;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.protobuf.Api;
import com.zjuse.bankingsystem.entity.Blacklist;
import com.zjuse.bankingsystem.mapper.BlacklistMapper;
import com.zjuse.bankingsystem.utils.ApiResult;


@Service
public class BlacklistService {

    @Autowired
    BlacklistMapper blacklistMapper;

    public ApiResult getBlacklist() {
        try {
            QueryWrapper wrapper = new QueryWrapper(); 
            List<Blacklist> blacklist = blacklistMapper.selectList(wrapper);
            ApiResult apiResult = new ApiResult(true, "success");
            apiResult.payload = blacklist;
            return apiResult;
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }
    public ApiResult isInblacklist(Long userId) {
        try {
            QueryWrapper wrapper = new QueryWrapper(); 
            wrapper.eq("user_id", userId);
            Boolean isIn = blacklistMapper.selectCount(wrapper) > 0;


            ApiResult apiResult = new ApiResult(true, "success");
            apiResult.payload = isIn;
            return apiResult;
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResult(false, e.getMessage());
        }
    }
    public ApiResult addBlacklist(Long userId, String reason) {
        try {
            QueryWrapper wrapper = new QueryWrapper<>();
            wrapper.eq("user_id", userId);
            if (blacklistMapper.selectCount(wrapper) > 0) {
                return new ApiResult(false, "user already in blacklist");
            }
            Blacklist blacklist = new Blacklist(userId, reason);
            blacklistMapper.insert(blacklist);
            return new ApiResult(true, "success");
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }
    public ApiResult removeBlacklist(Long userId) {
        try {
            QueryWrapper wrapper = new QueryWrapper(); 
            wrapper.eq("user_id", userId);
            
            if (blacklistMapper.selectCount(wrapper) == 0) {
                return new ApiResult(false, "user not in blacklist");
            }
            blacklistMapper.delete(wrapper);
            return new ApiResult(true, "success");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResult(false, e.getMessage());
        }
    }
}
