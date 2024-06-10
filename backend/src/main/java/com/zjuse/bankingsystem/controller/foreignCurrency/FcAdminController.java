package com.zjuse.bankingsystem.controller.foreignCurrency;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zjuse.bankingsystem.entity.foreignCurrency.DataOperator;
import com.zjuse.bankingsystem.service.foreignCurrency.FcAdminService;

@CrossOrigin
@RestController
@RequestMapping("/fc/admin")

public class FcAdminController {
    @Autowired
    private FcAdminService fcAdminService;

    @PostMapping("login")
    public RespResult FcAdminLogin(@RequestParam String name, @RequestParam String password) {
        System.out.println("loginAdmin where name = " + name + " and password = " + password);
        ApiResult apiResult = fcAdminService.FcAdminLogin(name, password);
        if (apiResult.ok) {
            return RespResult.success(apiResult.payload);
        } else {
            return RespResult.fail("用户名或密码错误");
        }
    }

    @GetMapping("home")
    public RespResult queryOperator() {
        ApiResult apiResult = fcAdminService.queryOperator();
        return RespResult.success(apiResult.payload);
    }

    @GetMapping("operationRecord")
    public RespResult queryOperationRecord() {
        ApiResult apiResult = fcAdminService.queryOperationRecord();
        return RespResult.success(apiResult.payload);
    }

    @PostMapping("home/insert")
    public RespResult insertDataOperator(@RequestParam String username, @RequestParam String password, @RequestParam String email,@RequestParam String phone_number,@RequestParam String data_operator_id,@RequestParam int add_permission,@RequestParam int delete_permission,@RequestParam int update_permission) {
        DataOperator dataOperator= new DataOperator(data_operator_id,username,password,email,phone_number,add_permission,update_permission,delete_permission);
        ApiResult apiResult = fcAdminService.insertDataOperator(dataOperator);
        return  RespResult.success(null);
    }

    @DeleteMapping("home/delete")
    public RespResult deleteDataOperator(@RequestParam String id) {
        ApiResult apiResult = fcAdminService.deleteDataOperator(id);
        return  RespResult.success(null);
    }

    @PostMapping("home/update")
    public RespResult updateOperatorPermission(@RequestParam String id, @RequestParam int addPermission, @RequestParam int updatePermission, @RequestParam int deletePermission) {
        ApiResult apiResult = fcAdminService.updateOperatorPermission(id, addPermission, updatePermission, deletePermission);
        return RespResult.success(null);
    }
}