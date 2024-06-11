package com.zjuse.bankingsystem.controller.foreignCurrency;

import com.zjuse.bankingsystem.service.foreignCurrency.ForeignCurrencyAccountService;
import com.zjuse.bankingsystem.utils.RespResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/fc/account")
@CrossOrigin(origins = "http://localhost:8080")
public class ForeignCurrencyAccountController {

    @Autowired
    private final ForeignCurrencyAccountService foreignCurrencyAccountService;

    public ForeignCurrencyAccountController(ForeignCurrencyAccountService foreignCurrencyAccountService) {
        this.foreignCurrencyAccountService = foreignCurrencyAccountService;
    }

    @GetMapping("/{credit_card_id}/{user_id}")
    public RespResult findAccountsByCreditCardId(@PathVariable String credit_card_id, @PathVariable String user_id) {
        // To do: check credit card id belong to user_ids
        return RespResult.success(foreignCurrencyAccountService.findAccountsByCreditCardId(credit_card_id));
    }
}