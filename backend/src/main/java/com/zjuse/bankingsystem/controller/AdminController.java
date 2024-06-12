package com.zjuse.bankingsystem.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.zjuse.bankingsystem.entity.loan.Officer;
import com.zjuse.bankingsystem.service.creditCard.CreditCardAdminService;
import com.zjuse.bankingsystem.service.deposite.CashierService;
import com.zjuse.bankingsystem.service.loan.OfficerService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/admin")
@PreAuthorize("@roleCheck.isRole('ADMIN')")
public class AdminController {
    @Autowired
    private CreditCardAdminService adminService;
    @Autowired
    private CashierService cashierService; 
    @Autowired
    private OfficerService officerService; 

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
    // 增加了修改出纳员密码接口
    @PostMapping("/counter/admin/cashier/modify")
    public RespResult modifyPassword(@RequestParam("id") Long id, @RequestParam("oldpassword") String oldpassword, @RequestParam("newpassword") String newpassword) {
        System.out.println("modifyCashier where id = " + id + " and oldpassword = " + oldpassword + " and newpassword = " + newpassword);
        ApiResult apiResult1 = cashierService.verifyCashier(id, oldpassword);
        if(!apiResult1.ok)
            return RespResult.fail(apiResult1.message);
        ApiResult apiResult = cashierService.changePassword(id, newpassword);
        if(!apiResult.ok)
            return RespResult.fail(apiResult.message);
        else
            return RespResult.success(apiResult.payload);
    }

    // 外汇相关
    @PostMapping("/add-officer")
    public String insertOfficer(@RequestBody Officer officer, HttpServletRequest request) {
        log.info(officer.toString()); 
        return officerService.insertOfficer(officer);
    }

    @PutMapping("/update-officer-password")
    public String updateOfficerPassword(@RequestParam String username, @RequestParam String newPassword) {
        return officerService.updateOfficerPassword(username, newPassword);
    }

    @PutMapping("/update-officer-permission")
    public String updateOfficerPermission(@RequestParam String username, @RequestParam String newPermission) {
        return officerService.updateOfficerPermission(username, newPermission);
    }

    @GetMapping("/get-officers")
    public IPage<Officer> getOfficers(@RequestParam int page, @RequestParam int pageSize) {
        return officerService.getOfficers(page, pageSize);
    }

    @DeleteMapping("/delete-officer/{id}")
    public String deleteOfficer(@PathVariable("id") int officer_id) {
        return officerService.deleteOfficer(officer_id);
    }
}
