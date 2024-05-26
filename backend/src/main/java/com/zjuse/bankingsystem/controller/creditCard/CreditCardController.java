package com.zjuse.bankingsystem.controller.creditCard;

import com.zjuse.bankingsystem.service.creditCard.CreditCardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.amqp.RabbitProperties;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.Date;

@RestController
public class CreditCardController {

    @Autowired
    private CreditCardService creditCardService;

    @GetMapping("/creditCard/customer/card")
    public RespResult getCardsByIdNumber(@RequestParam String id_number) {
        return RespResult.success(creditCardService.getCardsByIdNumber(id_number).payload);
    }

    @PostMapping("/creditCard/customer/card/register")
    public RespResult addNewCreditCard(@RequestParam String id_number, @RequestParam BigInteger card_limit, @RequestParam String password) {
        return RespResult.success(creditCardService.addNewCreditCardRequest(id_number, card_limit, password).payload);
    }

    @PostMapping("/creditCard/customer/card/modify")
    public RespResult modifyCreditCardPassword(@RequestParam BigInteger card_id, @RequestParam String password) {
//        System.out.println("card_id = " + card_id + " and password = " + password);
        return RespResult.success(creditCardService.modifyCreditCardPassword(card_id, password).payload);
    }

    @PostMapping("/creditCard/customer/card/update")
    public RespResult addModifyLimitRequest(@RequestParam String id_number, @RequestParam BigInteger id, @RequestParam BigInteger limit) {
        System.out.println("id_number = " + id_number + " and id = " + id + " and limit = " + limit);
        return RespResult.success(creditCardService.addModifyLimitRequest(id_number, id, limit).payload);
    }

    @PostMapping("/creditCard/customer/card/return")
    public RespResult returnMoney(@RequestParam BigInteger card_id, @RequestParam BigInteger amount) {
        System.out.println("card_id = " + card_id + " and amount = " + amount);
        return RespResult.success(creditCardService.returnMoney(card_id, amount).payload);
    }

    @GetMapping("/creditCard/customer/card/lost")
    public RespResult makeCreditCardLost(@RequestParam BigInteger card_id) {
        System.out.println("card_id = " + card_id);
        return RespResult.success(creditCardService.makeCreditCardLost(card_id).payload);
    }

    @GetMapping("/creditCard/customer/card/delete")
    public RespResult deleteCreditCard(@RequestParam BigInteger card_id) {
        System.out.println("delete card_id = " + card_id);
        return RespResult.success(creditCardService.deleteCreditCard(card_id).payload);
    }

    @PostMapping("/creditCard/admin/login/log")
    public RespResult loginAdmin(@RequestParam String name, @RequestParam String password) {
        System.out.println("loginAdmin where name = " + name + " and password = " + password);
        ApiResult apiResult = creditCardService.loginAdmin(name, password);
        if (apiResult.ok) {
            return RespResult.success(null);
        } else {
            return RespResult.fail("登录失败");
        }
    }

    @GetMapping("/creditCard/admin/inspector")
    public RespResult queryInspectors() {
        ApiResult apiResult = creditCardService.queryInspectors();
        return RespResult.success(apiResult.payload);
    }

    @PostMapping("/creditCard/admin/inspector/modify")
    public RespResult modifyInspectorPassword(@RequestParam Integer id, @RequestParam String password) {
        ApiResult apiResult = creditCardService.modifyInspectorPassword(id, password);
        return RespResult.success(null);
    }

    @PostMapping("/creditCard/admin/inspector/update")
    public RespResult modifyInspectorLevel(@RequestParam Integer id, @RequestParam Integer permission) {
        creditCardService.modifyInspectorLevel(id, permission);
        return RespResult.success(null);
    }

    @GetMapping("/creditCard/admin/inspector/delete")
    public RespResult deleteInspector(@RequestParam Integer id) {
        creditCardService.deleteInspector(id);
        return RespResult.success(null);
    }

    @PostMapping("/creditCard/admin/inspector/add")
    public RespResult addNewInspector(@RequestParam String name, @RequestParam String password, @RequestParam Integer permission) {
        creditCardService.addNewInspector(name, password, permission);
        return RespResult.success(null);
    }

    @PostMapping("/creditCard/inspector/login/log")
    public RespResult loginInspector(@RequestParam String name, @RequestParam String password) {
        ApiResult apiResult = creditCardService.loginInspector(name, password);
        if (apiResult.ok) {
            return RespResult.success(apiResult.payload);
        } else {
            return RespResult.fail("登录失败");
        }
    }

    @GetMapping("/creditCard/inspector/request")
    public RespResult queryRequestsByInspector(@RequestParam Integer permission) {
        ApiResult apiResult = creditCardService.queryRequestsByInspector(permission);
        return RespResult.success(apiResult.payload);
    }

    @GetMapping("/creditCard/inspector/request/accept")
    public RespResult acceptRequest(@RequestParam Integer id) {
        ApiResult apiResult = creditCardService.acceptRequest(id);
        return RespResult.success(apiResult.payload);
    }

    @GetMapping("/creditCard/inspector/request/reject")
    public RespResult rejectRequest(@RequestParam Integer id) {
        ApiResult apiResult = creditCardService.rejectRequest(id);
        return RespResult.success(apiResult.payload);
    }

    @GetMapping("/creditCard/customer/queryRequests")
    public RespResult queryRequestsByCustomer(@RequestParam String idNumber) {
        ApiResult apiResult = creditCardService.queryRequestsByCustomer(idNumber);
        return RespResult.success((apiResult.payload));
    }


    @PostMapping("/creditCard/customer/pay/add")
    public RespResult bankPay(@RequestParam BigInteger card_id, @RequestParam String id_number, @RequestParam BigInteger account, @DateTimeFormat(pattern = "yyyy-MM-dd") Date date, @RequestParam String password) {
        ApiResult apiResult = creditCardService.bankPay(card_id, id_number, password, account, date);
        if (apiResult.ok) {
            return RespResult.success(apiResult.payload);
        } else {
            return RespResult.fail(apiResult.message);
        }
    }

    @PostMapping("/creditCard/customer/simulation/query")
    public RespResult queryBills(@DateTimeFormat(pattern = "yyyy-MM-dd") Date start_date, @DateTimeFormat(pattern = "yyyy-MM-dd") Date end_date, @RequestParam String id_number) {
        System.out.println("start_date = " + start_date + " and end_date = " + end_date + " and id_number = " + id_number);
        ApiResult apiResult = creditCardService.queryBills(start_date, end_date, id_number);
        return RespResult.success(apiResult.payload);
    }

}
