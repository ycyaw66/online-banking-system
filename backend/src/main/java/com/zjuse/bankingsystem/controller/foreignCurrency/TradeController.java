package com.zjuse.bankingsystem.controller.foreignCurrency;

import com.zjuse.bankingsystem.entity.foreignCurrency.TradeRecord;
import com.zjuse.bankingsystem.entity.user.Card;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.foreignCurrency.TradeService;
import com.zjuse.bankingsystem.service.user.CardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * TradeController提供外汇交易的API接口。
 */
@RestController
@CrossOrigin
@RequestMapping("/fc/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @Autowired
    private CurrentUserService currentUserService; 

    @Autowired
    private CardService cardService; 

    /**
     * 执行外汇交易的接口。
     *
     * @return 交易结果消息
     */
    @PostMapping("/execute")
    public RespResult executeTrade(
            @RequestBody TradeRecord record) {
        // NO id will come fron front end so we need to get it from current user
        // Check record.getUserId() 与 record.getCreditCardId() 是否合法
        Long userId = (Long)currentUserService.getCurrentUserId().payload;
        if (String.valueOf(userId).equals(record.getUser_id())) 
            return RespResult.fail("用户id不符合");
        ApiResult res = cardService.getAllCardbyUserId(userId);
        record.setUser_id(String.valueOf(userId));
        if (!res.ok)
            return RespResult.fail(res.message); 
        List<Card> cardList = (List<Card>) res.payload; 
        boolean find = false;
        for (Card card : cardList) {
            if (String.valueOf(card.getCardId()).equals(record.getCredit_card_id())) {
                find = true;
                break ;
            }
        }
        if (!find)
            return RespResult.fail("卡号错误");
        return tradeService.executeTrade(record);
    }
}
