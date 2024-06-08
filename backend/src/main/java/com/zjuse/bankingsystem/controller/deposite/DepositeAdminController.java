package com.zjuse.bankingsystem.controller.deposite;

import com.zjuse.bankingsystem.service.deposite.DepositeAdminService;
import com.zjuse.bankingsystem.service.deposite.CashierService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class DepositeAdminController {
    @Autowired
    private DepositeAdminService adminService;

    @Autowired
    private CashierService cashierService;

    @PostMapping("/counter/admin/login/log")
    public RespResult loginAdmin(@RequestParam("id") Long id, @RequestParam("password") String password) {
        System.out.println("loginAdmin where id = '" + id + "' and password = '" + password + "'");
        ApiResult apiResult  = adminService.verifyAdmin(id, password);
        if(apiResult.ok)
            return RespResult.success(apiResult.payload);
        else
            return RespResult.fail(apiResult.message);
    }

    

    @PostMapping("/counter/admin/modify")
    public RespResult modifyPassword(@RequestParam("id") Long id, @RequestParam("password") String newpassword, @RequestParam("oldpassword")String oldpassword) {
        System.out.println("modifyPassword where id = '" + id + "' and password = '" + newpassword + "' and oldpassword = '" + oldpassword + "'");
        ApiResult apiResult1 = adminService.verifyAdmin(id, oldpassword);
        if(!apiResult1.ok)
            return RespResult.fail(apiResult1.message);
        ApiResult apiResult = adminService.ChangePassword(id, newpassword);
        if(apiResult.ok)
            return RespResult.success(apiResult.message);
        else
            return RespResult.fail(apiResult.message);
    }

    

    

    
}
