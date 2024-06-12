package com.zjuse.bankingsystem.controller.foreignCurrency;

import com.zjuse.bankingsystem.entity.user.Card;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.foreignCurrency.ForeignCurrencyAccountService;
import com.zjuse.bankingsystem.service.user.CardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/fc/account")
public class ForeignCurrencyAccountController {

    @Autowired
    private final ForeignCurrencyAccountService foreignCurrencyAccountService;

    @Autowired
    private CurrentUserService currentUserService; 

    @Autowired
    private CardService cardService; 

    public ForeignCurrencyAccountController(ForeignCurrencyAccountService foreignCurrencyAccountService) {
        this.foreignCurrencyAccountService = foreignCurrencyAccountService;
    }

    @GetMapping("/{credit_card_id}")
    public RespResult findAccountsByCreditCardId(@PathVariable String credit_card_id) {
        // To do: check credit card id belong to user_ids
        Long userId = (Long) currentUserService.getCurrentUserId().payload; 
        ApiResult res = cardService.getAllCardbyUserId(userId);
        if (!res.ok)
            return RespResult.fail(res.message); 
        List<Card> cardList = (List<Card>) res.payload; 
        boolean find = false;
        for (Card card : cardList) {
            if (String.valueOf(card.getCardId()).equals(credit_card_id)) {
                find = true;
                break ;
            }
        }
        if (!find)
            return RespResult.fail("卡号错误");
        return RespResult.success(foreignCurrencyAccountService.findAccountsByCreditCardId(credit_card_id));
    }
}