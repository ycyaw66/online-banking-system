package com.zjuse.bankingsystem.controller.user;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zjuse.bankingsystem.entity.user.User;
import com.zjuse.bankingsystem.entity.user.UserPrivilege;
import com.zjuse.bankingsystem.service.user.UserPrivilegeService;
import com.zjuse.bankingsystem.service.user.UserService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/privilege")
public class PrivilegeController {

    @Autowired
    UserPrivilegeService userPrivilegeService;
    @Autowired
    UserService userService; 

    @PostMapping("/update")
    public RespResult updatePrivilege(@RequestBody UserPrivilege userPrivilege) {
        ApiResult apiResult = userPrivilegeService.modifyUserPrivilege(userPrivilege);
        if (apiResult.ok) {
            return RespResult.success(null);
        } 
        else {
            return RespResult.fail(apiResult.message);
        }
    }

    static class RespUser {
        @JsonProperty("user_id")
        Long userId; 
        String username; 
        List<Map<String, String>> permissions; 
    }

    @GetMapping("/get")
    public RespResult updatePrivilege() {
        ApiResult apiResult = userPrivilegeService.getAllUserPrivilege();
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        List<UserPrivilege> privilegeList = (List<UserPrivilege>)apiResult.payload; 
        List<Object> RespList = new ArrayList<>(); 
        for (UserPrivilege privilege : privilegeList) {
            User user = (User) userService.getUserById(privilege.getUserId()).payload;
            RespList.add(new HashMap<String, Object>(){{
                put("user_id", privilege.getUserId());
                put("username",  user.getUsername());
                put("permissions", new ArrayList<>(){{
                    add(new HashMap<>(){{
                        put("name", "支付");
                        put("enabled", privilege.isPayment());
                    }});
                    add(new HashMap<>(){{
                        put("name", "转账");
                        put("enabled", privilege.isTransfer());
                    }});
                    add(new HashMap<>(){{
                        put("name", "收款");
                        put("enabled", privilege.isReceive());
                    }});
                }});
            }}); 
        }
        return RespResult.success(RespList);
    }
}
