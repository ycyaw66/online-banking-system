package com.zjuse.bankingsystem.controller.creditCard;

import com.zjuse.bankingsystem.service.creditCard.CustomerService;
import com.zjuse.bankingsystem.utils.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping("/creditCard/customer/card")
    public RespResult getCardsByIdNumber(@RequestParam String id_number){
        return RespResult.success(customerService.getCardsByIdNumber(id_number).payload);
    }
}
