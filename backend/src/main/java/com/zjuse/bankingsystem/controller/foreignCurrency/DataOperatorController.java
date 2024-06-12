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

    @DeleteMapping("/delete/{data_operator_id}")
    public RespResult delete(String data_operator_id) {
        ApiResult result = new ApiResult(false, "");
        try{
            DataOperator data_operator = dataOperatorService.selectDataOperatorByUsername(data_operator_id);
            dataOperatorService.deleteDataOperator(data_operator.getData_operator_id());
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
                    DataOperatorInfo data_operator = dataOperatorService.selectDataOperatorByUsernameAndPassword(update_info.data_operator_id, update_info.password);
                    dataOperatorService.updateDataOperatorPassword(data_operator.getData_operator_id(), update_info.new_password, update_info.password);
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
            
        return RespResult.success(dataOperatorService.selectDataOperatorByUsername(data_operator_id));
        }catch(Exception e){
            return RespResult.fail(e.getMessage());
        }
    }

}
