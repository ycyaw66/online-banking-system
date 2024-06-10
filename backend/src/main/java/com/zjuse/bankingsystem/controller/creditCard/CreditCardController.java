package com.zjuse.bankingsystem.controller.creditCard;

import java.math.BigDecimal;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.creditCard.CreditCardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/credit-card")
public class CreditCardController {
    @Autowired
    private CreditCardService creditCardService;
    @Autowired
    private CurrentUserService currentUserService; 
    
    @GetMapping("/getAllCard")
    public RespResult getCardsByIdNumber() {
        ApiResult apiResult = currentUserService.getCurrentUserIdNumber();
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        String idNumber = (String) apiResult.payload;
        apiResult = creditCardService.getCardsByIdNumber(idNumber);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        return RespResult.success(apiResult.payload);
    }

    @PostMapping("/register")
    public RespResult addNewCreditCard(@RequestParam BigDecimal card_limit, @RequestParam String password) {
        ApiResult apiResult = currentUserService.getCurrentUserIdNumber();
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        String idNumber = (String) apiResult.payload;
        apiResult = creditCardService.addNewCreditCardRequest(idNumber, card_limit, password);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        return RespResult.success(apiResult.payload);
    }

    @PostMapping("/modify-password")
    public RespResult modifyCreditCardPassword(@RequestParam Long card_id, @RequestParam String old_password, @RequestParam String new_password) {
        ApiResult apiResult = creditCardService.checkCreditCardPassword(card_id, old_password);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }

        apiResult = creditCardService.modifyCreditCardPassword(card_id, new_password);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        return RespResult.success();
    }

    @PostMapping("/update-limit")
    public RespResult addModifyLimitRequest(@RequestParam Long card_id, @RequestParam BigDecimal limit, @RequestParam String password) {
        ApiResult apiResult = creditCardService.checkCreditCardPassword(card_id, password);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }

        apiResult = currentUserService.getCurrentUserIdNumber();
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        String idNumber = (String) apiResult.payload;
        apiResult = creditCardService.addModifyLimitRequest(idNumber, card_id, limit); 
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        return RespResult.success();
    }

    @PostMapping("/return")
    public RespResult returnMoney(@RequestParam Long card_id, @RequestParam BigDecimal amount, @RequestParam String password) {
        ApiResult apiResult = creditCardService.checkCreditCardPassword(card_id, password);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }

        apiResult = creditCardService.returnMoney(card_id, amount);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        return RespResult.success();
    }

    @PostMapping("/lost")
    public RespResult makeCreditCardLost(@RequestParam Long card_id, @RequestParam String password) {
        ApiResult apiResult = creditCardService.checkCreditCardPassword(card_id, password);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        apiResult = creditCardService.makeCreditCardLost(card_id, password); 
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        return RespResult.success();
    }

    @PostMapping("/delete")
    public RespResult deleteCreditCard(@RequestParam Long card_id, @RequestParam String password) {
        ApiResult apiResult = creditCardService.checkCreditCardPassword(card_id, password);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        apiResult = creditCardService.deleteCreditCard(card_id); 
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        return RespResult.success();
    };

    @GetMapping("/query-request")
    public RespResult queryRequestsByCustomer() {
        ApiResult apiResult = currentUserService.getCurrentUserIdNumber();
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        String idNumber = (String) apiResult.payload;
        apiResult = creditCardService.queryRequestsByCustomer(idNumber);
        return RespResult.success((apiResult.payload));
    }


    @PostMapping("/pay")
    public RespResult bankPay(@RequestParam Long card_id, @RequestParam BigDecimal amount, @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam String password) {
        ApiResult apiResult = creditCardService.bankPay(card_id, password, amount, date);
        if (apiResult.ok) {
            return RespResult.success(apiResult.payload);
        } else {
            return RespResult.fail(apiResult.message);
        }
    }

    @PostMapping("/bills/query")
    public RespResult queryBills(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start_date, @DateTimeFormat(pattern = "yyyy-MM-dd") Date end_date) {
        ApiResult apiResult = currentUserService.getCurrentUserIdNumber();
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        String idNumber = (String) apiResult.payload;
        apiResult = creditCardService.queryBills(start_date, end_date, idNumber);
        if (!apiResult.ok) {
            return RespResult.fail(apiResult.message);
        }
        return RespResult.success(apiResult.payload);
    }
}
