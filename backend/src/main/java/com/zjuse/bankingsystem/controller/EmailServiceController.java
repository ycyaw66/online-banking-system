package com.zjuse.bankingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.service.EmailViladService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;

@RestController
@RequestMapping("email")
public class EmailServiceController {
    
    @Data
    private class Receiver {
        String uuid;
        @NonNull
        String email;
    }

    @Autowired
    EmailViladService emailViladService;

    @PostMapping("")
    public RespResult emailValidRequest(@RequestBody Receiver receiver) {
        ApiResult apiResult = emailViladService.sendEmail(receiver.email, receiver.uuid);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        if (receiver.uuid == null) {
            receiver.uuid = (String) apiResult.payload;
        }
        return RespResult.success(receiver.uuid);
    }

}
