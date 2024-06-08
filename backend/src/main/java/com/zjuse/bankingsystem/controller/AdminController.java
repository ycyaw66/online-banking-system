package com.zjuse.bankingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.service.creditCard.CreditCardAdminService;
import com.zjuse.bankingsystem.service.creditCard.CreditCardService;
import com.zjuse.bankingsystem.service.deposite.CashierService;
import com.zjuse.bankingsystem.service.deposite.CashierService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private CreditCardAdminService adminService;
    @Autowired
    private CashierService cashierService; 


    @PostMapping("/creditCard/admin/login/log")
    public RespResult loginAdmin(@RequestParam String name, @RequestParam String password) {
        System.out.println("loginAdmin where name = " + name + " and password = " + password);
        ApiResult apiResult = adminService.loginAdmin(name, password);
        if (apiResult.ok) {
            return RespResult.success(null);
        } else {
            return RespResult.fail("登录失败");
        }
    }

    @GetMapping("/creditCard/admin/inspector")
    public RespResult queryInspectors() {
        ApiResult apiResult = adminService.queryInspectors();
        return RespResult.success(apiResult.payload);
    }

    @PostMapping("/inspector/modify")
    public RespResult modifyInspectorPassword(@RequestParam Integer id, @RequestParam String password) {
        ApiResult apiResult = adminService.modifyInspectorPassword(id, password);
        return RespResult.success(null);
    }

    @PostMapping("/inspector/update")
    public RespResult modifyInspectorLevel(@RequestParam Integer id, @RequestParam Integer permission) {
        adminService.modifyInspectorLevel(id, permission);
        return RespResult.success(null);
    }

    @PostMapping("/inspector/delete")
    public RespResult deleteInspector(@RequestParam Integer id) {
        adminService.deleteInspector(id);
        return RespResult.success(null);
    }

    @PostMapping("/inspector/add")
    public RespResult addNewInspector(@RequestParam String name, @RequestParam String password, @RequestParam Integer permission) {
        adminService.addNewInspector(name, password, permission);
        return RespResult.success(null);
    }

    @GetMapping("/counter/admin/cashier")
    public RespResult queryCashier() {
        ApiResult apiResult  = cashierService.getAllCashier();
        if(apiResult.ok)
            return RespResult.success(apiResult.payload);
        else
            return RespResult.fail(apiResult.message);
    }

    @DeleteMapping("/counter/admin/cashier/delete")
    public RespResult deleteCashier(@RequestParam("id") Long id) {
        System.out.println("deleteCashier where id = '" + id + "'");
        ApiResult apiResult = cashierService.deleteCashierById(id);
        if(apiResult.ok)
            return RespResult.success(apiResult.message);
        else
            return RespResult.fail(apiResult.message);
    }

    @PostMapping("/counter/admin/cashier/add")
    public RespResult addCashier(@RequestParam("password") String password, @RequestParam("username")String username,@RequestParam("authority")int authority) {
        System.out.println("addCashier where password = '" + password + "' and username = '" + username + "'");
        ApiResult apiResult = cashierService.addCashier(username,password,authority);
        if(apiResult.ok)
            return RespResult.success(apiResult.payload);
        else
            return RespResult.fail(apiResult.message);
    }

    @PostMapping("/counter/admin/cashier/authority")
    public RespResult addAuthority(@RequestParam("id") Long id, @RequestParam("authority")int authority) {
        System.out.println("addAuthority where id = '" + id + "' and authority = '" + authority + "'");
        ApiResult apiResult = cashierService.changeAuthority(id, authority);
        if(apiResult.ok)
            return RespResult.success(apiResult.message);
        else
            return RespResult.fail(apiResult.message);
    }

}
