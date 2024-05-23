package com.zjuse.bankingsystem.controller;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zjuse.bankingsystem.service.UserAndCardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("option")
public class lossController {

    @Data
    @AllArgsConstructor
    class Receiver {
        @JsonProperty("card_id")
        @NonNull
        Long cardId;
        @NonNull
        String password;
    };

    @Autowired
    UserAndCardService userAndCardService;

    @PostMapping("transfer")
    public RespResult loss(@RequestBody Receiver receiver) {
        ApiResult apiResult = userAndCardService.loss(receiver.getCardId(), receiver.getPassword());
        if (apiResult.ok) {
            return RespResult.success(null);
        } 
        else {
            return RespResult.fail(apiResult.message);
        }
    }
}
