package com.zjuse.bankingsystem.service.foreignCurrency;
import com.zjuse.bankingsystem.entity.foreignCurrency.DataOperator;
import com.zjuse.bankingsystem.entity.foreignCurrency.FcAdministrator;
import com.zjuse.bankingsystem.entity.foreignCurrency.HistoryOperationRecord;
import com.zjuse.bankingsystem.mapper.foreignCurrency.FcAdminMapper;
import com.zjuse.bankingsystem.utils.ApiResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FcAdminService {
    @Autowired
    private FcAdminMapper fcAdminMapper;

    public ApiResult FcAdminLogin(String name, String password) {
        FcAdministrator fcAdmin = fcAdminMapper.FcAdminLogin(name, password);
        if (fcAdmin == null) {
            return new ApiResult(false, "登录失败");
        } else {
            return new ApiResult(true, "登录成功", fcAdmin);
        }
    }

    public ApiResult queryOperator(){
        List<DataOperator> operators= fcAdminMapper.queryOperator();
        return new ApiResult(true, operators);
    }

    public ApiResult queryOperationRecord(){
        List<HistoryOperationRecord> records = fcAdminMapper.queryOperationRecord();
        return new ApiResult(true, records);
    }

    public ApiResult insertDataOperator(DataOperator dataOperator){
        int a=fcAdminMapper.insertOperator(dataOperator);
        if(a==1){
            return new ApiResult(true, "添加成功");
        }
       return new ApiResult(false,"添加失败");
    }

    public ApiResult deleteDataOperator(String id){
       int a =  fcAdminMapper.deleteOperator(id);
       if(a==1) {
           return new ApiResult(true, "删除成功");
       }else{
           return new ApiResult(false,"删除失败");
       }
    }

    public ApiResult updateOperatorPermission(String id,int addPermission,int updatePermission,int deletePermission){
        int a = fcAdminMapper.updateOperatorPermission(id, addPermission, updatePermission, deletePermission);
        if(a==1) {
            return new ApiResult(true, "修改成功");
        }else{
            return new ApiResult(false,"修改失败");
        }
    }
}
