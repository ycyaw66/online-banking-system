package com.zjuse.bankingsystem.controller.user;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zjuse.bankingsystem.entity.user.Blacklist;
import com.zjuse.bankingsystem.entity.user.User;
import com.zjuse.bankingsystem.service.user.BlacklistService;
import com.zjuse.bankingsystem.service.user.UserService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import io.micrometer.common.lang.NonNull;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/blacklist")
public class BlacklistController {

    @Autowired
    BlacklistService blacklistService;
    @Autowired
    UserService userService; 

    @PostMapping("add")
    public RespResult addBlacklist(@RequestBody Blacklist blacklist) {
        ApiResult apiResult = blacklistService.addBlacklist(blacklist.getUserId(), blacklist.getReason());
        if (apiResult.ok) {
            return RespResult.success(null);
        }
        else {
            return RespResult.fail(apiResult.message);
        }
    }

    @Data
    static class Receiver {
        @JsonProperty("user_id")
        @NonNull
        Long userId;
    }

    @DeleteMapping("/remove")
    public RespResult removeBlacklist(@RequestBody Receiver receiver) {
        ApiResult apiResult = blacklistService.removeBlacklist(receiver.getUserId());
        if (apiResult.ok) {
            return RespResult.success(null);
        }
        else {
            return RespResult.fail(apiResult.message);
        }
    }

    @GetMapping("get")
    public RespResult getBlackList() {
        ApiResult apiResult = blacklistService.getBlacklist();
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        List<Blacklist> blacklist = ((List<Blacklist>)apiResult.payload);
        List<Object> RespList = new ArrayList<>(); 
        for (Blacklist tt : blacklist) {
            User user = (User)userService.getUserById(tt.getUserId()).payload;
            RespList.add(new HashMap<>(){{
                put("user_id", tt.getUserId());
                put("username", user.getUsername());
                put("reason", tt.getReason());
            }});
        }
        return RespResult.success(RespList);
    }

}
