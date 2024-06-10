package com.zjuse.bankingsystem.controller.foreignCurrency;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.entity.foreignCurrency.DataOperator;
import com.zjuse.bankingsystem.model.DataOperatorInfo;
import com.zjuse.bankingsystem.model.DataOperatorRegister;
import com.zjuse.bankingsystem.model.LoginInfo;
import com.zjuse.bankingsystem.model.UpdateInfo;
import com.zjuse.bankingsystem.service.foreignCurrency.DataOperatorService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@CrossOrigin
@RequestMapping("/fc/data_operator")
public class DataOperatorController {

    @Autowired
    private DataOperatorService dataOperatorService;

    @PostMapping("/start/register")
    public RespResult register(@RequestBody DataOperatorRegister dataOperatorRegister) {
        DataOperator entity = new DataOperator("", dataOperatorRegister.getUsername(), dataOperatorRegister.getPassword(), dataOperatorRegister.getEmail(), dataOperatorRegister.getPhone(), 0, 0, 0);
        if(!dataOperatorRegister.getConfirm().equals("111")){
            return RespResult.fail("邀请码错误");
        }
        try{
            dataOperatorService.register(entity);
        }catch(Exception e){
            return RespResult.fail(e.getMessage());
        }
        return RespResult.success("注册成功");

    }

    @PostMapping("/start/login")
    public RespResult login(@RequestBody LoginInfo loginInfo) {
        ApiResult result = new ApiResult(false, "");
        try{
            DataOperatorInfo dt = dataOperatorService.selectDataOperatorByUsernameAndPassword(loginInfo.getUsername(), loginInfo.getPassword());
            if(dt != null) {
                result.ok = true;
                result.message = "登录成功";
                result.payload = dt;
            } else {
                result.message = "用户名或密码错误";
            }
        }catch(Exception e){
            return RespResult.fail(e.getMessage());
        }
        return new RespResult(0, result.message, result.payload);
    }

    @DeleteMapping("/delete/{data_operator_id}")
    public RespResult delete(String data_operator_id) {
        ApiResult result = new ApiResult(false, "");
        try{
            dataOperatorService.deleteDataOperator(data_operator_id);
        }catch(Exception e){
            return RespResult.fail(e.getMessage());
        }
        result.ok = true;
        result.message = "删除成功";
        return RespResult.success(result.message);
    }

    @PutMapping("/update_info")
    public RespResult updateInfomation(@RequestBody UpdateInfo update_info) {
        try{
            if(!update_info.new_password.equals(""))
            {
                try{
                    dataOperatorService.updateDataOperatorPassword(update_info.data_operator_id, update_info.new_password, update_info.password);
                }catch(Exception e){
                    return RespResult.fail(e.getMessage());
                }
                return RespResult.success("修改密码成功");
            }
            dataOperatorService.updateInfomation(update_info);
        }catch(Exception e){
            return RespResult.fail(e.getMessage());
        }
        return RespResult.success("修改成功");
    }

    @GetMapping("/account/{data_operator_id}")
    public RespResult getInfomation(@PathVariable String data_operator_id) {
        try{
            
        return RespResult.success(dataOperatorService.selectDataOperatorById(data_operator_id));
        }catch(Exception e){
            return RespResult.fail(e.getMessage());
        }
    }

}
