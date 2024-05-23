package com.zjuse.bankingsystem.controller;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zjuse.bankingsystem.entity.Card;
import com.zjuse.bankingsystem.service.CardService;
import com.zjuse.bankingsystem.service.UserAndCardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import io.micrometer.common.lang.NonNull;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;



@RestController
@RequestMapping("card")
public class CardController {

    @Data
    @AllArgsConstructor
    class Receiver {
        @JsonProperty("user_id")
        @NonNull
        Long userId;
    };

    @Autowired
    UserAndCardService userAndCardService;

    @GetMapping("")
    public RespResult showCard(@RequestParam Long userId) {
        if (userId == null) {
            return RespResult.fail("userId is null");
        }
        ApiResult apiResult = userAndCardService.getAllCard(userId);
        if (apiResult.ok) {
            return RespResult.success((List<Card>)apiResult.payload);
        } 
        else {
            return RespResult.fail(apiResult.message);
        }
    }
}
