package com.zjuse.bankingsystem.service;

import org.springframework.beans.factory.annotation.Autowired;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.protobuf.Api;
import com.zjuse.bankingsystem.entity.UserPrivilege;
import com.zjuse.bankingsystem.mapper.UserPrivilegeMapper;
import com.zjuse.bankingsystem.utils.ApiResult;

public class UserPrivilegeService {
    @Autowired
    UserPrivilegeMapper userprivilegeMapper;
    public ApiResult getUserPrivilege(Long userId) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("userId", userId);
            UserPrivilege userPrivilege = userprivilegeMapper.selectOne(wrapper);
            if (userPrivilege == null) {
                return new ApiResult(false, "user not find");
            }
            ApiResult apiResult = new ApiResult(true, "success");
            apiResult.payload = userPrivilege;
            return apiResult;
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }
    }
    public ApiResult ModifyUserPrivilege(UserPrivilege userPrivilege) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("userId", userPrivilege.getUserId());
            
            userprivilegeMapper.update(userPrivilege, wrapper);
            return new ApiResult(true, "success");
        }
        catch (Exception e) {
            return new ApiResult(false, e.getMessage());
        }

    }
}
