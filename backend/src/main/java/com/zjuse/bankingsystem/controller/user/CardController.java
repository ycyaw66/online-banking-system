package com.zjuse.bankingsystem.controller.user;

import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zjuse.bankingsystem.entity.user.Card;
import com.zjuse.bankingsystem.entity.user.History;
import com.zjuse.bankingsystem.entity.user.HistoryCondition;
import com.zjuse.bankingsystem.entity.user.User;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.user.CardService;
import com.zjuse.bankingsystem.service.user.UserAndCardService;
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
@RequestMapping("/card")
public class CardController {

    @Data
    @AllArgsConstructor
    class Receiver {
        @JsonProperty("user_id")
        @NonNull
        Long userId;
    };

    @Autowired
    CardService cardService;
    @Autowired
    CurrentUserService currentUserService; 
    @Autowired
    UserAndCardService userAndCardService; 

    @GetMapping("")
    public RespResult showCard() {
        ApiResult apiResult = currentUserService.getCurrentUser(); 
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        User user = (User) apiResult.payload; 
        apiResult = cardService.getAllCardbyUserId(user.getId());
        if (apiResult.ok) {
            return RespResult.success((List<Card>)apiResult.payload);
        } 
        else {
            return RespResult.fail(apiResult.message);
        }
    }

    @GetMapping("/balance")
    public RespResult getBalance(@RequestParam Long cardId,@RequestParam String password) {
        ApiResult apiResult = userAndCardService.getBalance(cardId, password);
        if (apiResult.ok) {
            return RespResult.success((((BigDecimal)apiResult.payload).toString()));
        }
        else {
            return RespResult.fail(apiResult.message);
        }
    }

    @Data
    @AllArgsConstructor
    class LossReceiver {
        @JsonProperty("card_id")
        @NonNull
        Long cardId;
        @NonNull
        String password;
    };

    @PostMapping("loss")
    public RespResult loss(@RequestBody LossReceiver receiver) {
        ApiResult apiResult = userAndCardService.loss(receiver.getCardId(), receiver.getPassword());
        if (apiResult.ok) {
            return RespResult.success(null);
        } 
        else {
            return RespResult.fail(apiResult.message);
        }
    }

    @PostMapping("/history")
    public RespResult history(@RequestBody HistoryCondition historyCondition) {
        ApiResult apiResult = userAndCardService.history(historyCondition);
        if (apiResult.ok) {
            return RespResult.success((List<History>)apiResult.payload);
        }
        else {
            return RespResult.fail(apiResult.message);
        }
    }

    @Data
    @AllArgsConstructor
    class ValidReceiver {
        @JsonProperty("card_id")
        @NonNull
        Long cardId;
        @NonNull
        String password;
    };

    @PostMapping("/valid")
    public RespResult valid(@RequestBody ValidReceiver validReceiver) {
        ApiResult apiResult = userAndCardService.valid(validReceiver.getCardId(), validReceiver.getPassword());
        if (apiResult.ok) {
            return RespResult.success();
        }
        else {
            return RespResult.fail(apiResult.message);
        }
    }

    @Data
    @AllArgsConstructor
    class BindingReceiver {
        @JsonProperty("card_id")
        @NonNull
        Long cardId;
        @JsonProperty("user_id")
        @NonNull
        Long userId;
        @NonNull
        String password;
    };
    
    @PostMapping("binding")
    public RespResult binding(@RequestBody BindingReceiver receiver) {
        ApiResult apiResult = userAndCardService.valid(receiver.getCardId(), receiver.getPassword());
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        apiResult = cardService.bindUserAndCard(receiver.getUserId(), receiver.getCardId());
        if (apiResult.ok) {
            return RespResult.success();
        }
        else {
            return RespResult.fail(apiResult.message);
        }
    }
}
