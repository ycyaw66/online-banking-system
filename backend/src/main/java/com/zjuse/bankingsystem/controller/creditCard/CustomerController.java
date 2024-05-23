package com.zjuse.bankingsystem.controller.creditCard;

import com.zjuse.bankingsystem.service.creditCard.CustomerService;
import com.zjuse.bankingsystem.utils.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/creditCard/customer/card")
    public RespResult getCardsByIdNumber(@RequestParam String id_number) {
        return RespResult.success(customerService.getCardsByIdNumber(id_number).payload);
    }

    @PostMapping("/creditCard/customer/card/register")
    public RespResult addNewCreditCard(@RequestParam String id_number, @RequestParam BigInteger card_limit, @RequestParam String password) {
        return RespResult.success(customerService.addNewCreditCard(id_number, card_limit, password).payload);
    }

    @PostMapping("/creditCard/customer/card/modify")
    public RespResult modifyCreditCardPassword(@RequestParam BigInteger card_id, @RequestParam String password) {
//        System.out.println("card_id = " + card_id + " and password = " + password);
        return RespResult.success(customerService.modifyCreditCardPassword(card_id, password).payload);
    }

    @PostMapping("/creditCard/customer/card/update")
    public RespResult addModifyLimitRequest(@RequestParam String id_number, @RequestParam BigInteger id, @RequestParam BigInteger limit) {
        System.out.println("id_number = " + id_number + " and id = " + id + " and limit = " + limit);
        return RespResult.success(customerService.addModifyLimitRequest(id_number, id, limit).payload);
    }

    @PostMapping("/creditCard/customer/card/return")
    public RespResult returnMoney(@RequestParam BigInteger card_id, @RequestParam BigInteger amount) {
        System.out.println("card_id = " + card_id + " and amount = " + amount);
        return RespResult.success(customerService.returnMoney(card_id, amount).payload);
    }
}
