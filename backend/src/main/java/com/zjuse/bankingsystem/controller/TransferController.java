package com.zjuse.bankingsystem.controller;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zjuse.bankingsystem.service.UserAndCardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import io.micrometer.common.lang.NonNull;
import lombok.Data;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("transfer")
public class TransferController {

    @Data
    class Receiver {
        
        @JsonProperty("target_card")
        @NonNull
        Long targetCard;

        @JsonProperty("card_id")
        @NonNull
        Long cardId;

        @NonNull
        String password; 

        @NonNull
        String amount;

        @NonNull
        String remark;
    };

    @Autowired
    UserAndCardService userAndCardService;

    @PostMapping("")
    public RespResult transfer(@RequestBody Receiver receiver) {
        try {
            BigDecimal bigDecimal = new BigDecimal(receiver.amount);
            
            ApiResult apiResult = userAndCardService.transfor(receiver.getCardId(), receiver.getTargetCard(), bigDecimal, receiver.getPassword(), receiver.getRemark());
            if (apiResult.ok) {
                return RespResult.success(null);
            } 
            else {
                return RespResult.fail(apiResult.message);
            }
        }
        catch (Exception e) {
            return RespResult.fail(e.getMessage());
        }
    }
}
