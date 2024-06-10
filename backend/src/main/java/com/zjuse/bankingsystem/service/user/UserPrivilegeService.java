package com.zjuse.bankingsystem.service.user;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjuse.bankingsystem.entity.user.UserPrivilege;
import com.zjuse.bankingsystem.mapper.user.UserPrivilegeMapper;
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

    public boolean checkPayment(Long userId) throws Exception {
        ApiResult apiResult = getUserPrivilege(userId);
        if (apiResult.ok == false) {
            throw new Exception(apiResult.message);
        }
        UserPrivilege userPrivilege = (UserPrivilege) apiResult.payload;
        return userPrivilege.isPayment();
    }

    public boolean checkTransfer(Long userId) throws Exception {
        ApiResult apiResult = getUserPrivilege(userId);
        if (apiResult.ok == false) {
            throw new Exception(apiResult.message);
        }
        UserPrivilege userPrivilege = (UserPrivilege) apiResult.payload;
        return userPrivilege.isTransfer();
    }

    public boolean checkReceive(Long userId) throws Exception {
        ApiResult apiResult = getUserPrivilege(userId);
        if (apiResult.ok == false) {
            throw new Exception(apiResult.message);
        }
        UserPrivilege userPrivilege = (UserPrivilege) apiResult.payload;
        return userPrivilege.isReceive();
    }

}
