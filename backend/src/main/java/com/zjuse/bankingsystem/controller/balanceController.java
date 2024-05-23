package com.zjuse.bankingsystem.controller;

import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.service.UserAndCardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import io.micrometer.common.lang.NonNull;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("balance")
public class balanceController {

    @Autowired
    UserAndCardService userAndCardService;


    @GetMapping("get")
    public RespResult getBalance(@RequestParam Long cardId,@RequestParam String password) {
        ApiResult apiResult = userAndCardService.getBalance(cardId, password);
        if (apiResult.ok) {
            return RespResult.success((((BigDecimal)apiResult.payload).toString()));
        }
        else {
            return RespResult.fail(apiResult.message);
        }
    }

}
