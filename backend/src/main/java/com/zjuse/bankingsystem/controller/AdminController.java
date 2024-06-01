package com.zjuse.bankingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.service.AdminService;
import com.zjuse.bankingsystem.service.CreditCardService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private AdminService adminService;


    @PostMapping("/login")
    public RespResult loginAdmin(@RequestParam String name, @RequestParam String password) {
        ApiResult apiResult = adminService.loginAdmin(name, password);
        if (apiResult.ok) {
            return RespResult.success(null);
        } else {
            return RespResult.fail("登录失败");
        }
    }

    @GetMapping("/inspector")
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
}
