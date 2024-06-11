package com.zjuse.bankingsystem.controller.user;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zjuse.bankingsystem.entity.user.UserPrivilege;
import com.zjuse.bankingsystem.service.user.UserPrivilegeService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import io.micrometer.common.lang.NonNull;
import lombok.Data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("privilege")
public class PrivilegeController {

    @Autowired
    UserPrivilegeService userPrivilegeService;

    @PostMapping("update")
    public RespResult updatePrivilege(@RequestBody UserPrivilege userPrivilege) {
        ApiResult apiResult = userPrivilegeService.modifyUserPrivilege(userPrivilege);
        if (apiResult.ok) {
            return RespResult.success(null);
        } 
        else {
            return RespResult.fail(apiResult.message);
        }
    }

    @Data
    static class Receiver {
        @NonNull
        @JsonProperty("user_id")
        Long userId;
    }

    @GetMapping("get")
    public RespResult updatePrivilege(@RequestBody Receiver receiver) {
        ApiResult apiResult = userPrivilegeService.getUserPrivilege(receiver.getUserId());
        if (apiResult.ok) {
            return RespResult.success((UserPrivilege)apiResult.payload);
        } 
        else {
            return RespResult.fail(apiResult.message);
        }
    }
}
