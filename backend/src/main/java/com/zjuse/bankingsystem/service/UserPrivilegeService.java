package com.zjuse.bankingsystem.service;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.google.protobuf.Api;
import com.zjuse.bankingsystem.entity.UserPrivilege;
import com.zjuse.bankingsystem.mapper.UserPrivilegeMapper;
import com.zjuse.bankingsystem.utils.ApiResult;

@Service
public class UserPrivilegeService {
    @Autowired
    UserPrivilegeMapper userprivilegeMapper;
    public ApiResult getUserPrivilege(Long userId) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("user_id", userId);
            UserPrivilege userPrivilege = userprivilegeMapper.selectOne(wrapper);
            if (userPrivilege == null) {
                return new ApiResult(false, "user not find");
            }
            ApiResult apiResult = new ApiResult(true, "success");
            apiResult.payload = userPrivilege;
            return apiResult;
        }
        catch (Exception e) {
            
            System.out.println(e.getMessage());
            return new ApiResult(false, e.getMessage());
        }
    }
    public ApiResult modifyUserPrivilege(UserPrivilege userPrivilege) {
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq("user_id", userPrivilege.getUserId());
            if (userprivilegeMapper.selectCount(wrapper) == 0) {
                return new ApiResult(false, "user not find");
            }
            userprivilegeMapper.update(userPrivilege, wrapper);
            return new ApiResult(true, "success");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            return new ApiResult(false, e.getMessage());
        }

    }

    public ApiResult insertUserPrivilege(UserPrivilege userPrivilege) {
        try {
            userprivilegeMapper.insert(userPrivilege);
            return new ApiResult(true, "success");
        }
        catch (Exception e) {

            System.out.println(e.getMessage());
            return new ApiResult(false, e.getMessage());
        }

    }

}
