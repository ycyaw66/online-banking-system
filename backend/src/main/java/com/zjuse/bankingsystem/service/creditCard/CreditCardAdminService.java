package com.zjuse.bankingsystem.service.creditCard;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zjuse.bankingsystem.entity.creditCard.CreditCardInspector;
import com.zjuse.bankingsystem.mapper.creditCard.InspectorMapper;
import com.zjuse.bankingsystem.utils.ApiResult;

@Service
public class CreditCardAdminService {
    @Autowired
    private InspectorMapper inspectorMapper; 

    public ApiResult queryInspectors() {
        List<CreditCardInspector> creditCardInspectors = inspectorMapper.queryInspectors();
        return new ApiResult(true, creditCardInspectors);
    }

    public ApiResult modifyInspectorPassword(Integer id, String password) {
        inspectorMapper.modifyInspectorPassword(id, password);
        return new ApiResult(true, "修改成功");
    }

    public ApiResult modifyInspectorLevel(Integer id, Integer permission) {
        inspectorMapper.modifyInspectorLevel(id, permission);
        return new ApiResult(true, "修改成功");
    }

    public ApiResult deleteInspector(Integer id) {
        inspectorMapper.deleteInspector(id);
        return new ApiResult(true, "删除成功");
    }

    public ApiResult addNewInspector(String name, String password, Integer permission) {
        inspectorMapper.addNewInspector(name, password, permission);
        return new ApiResult(true, "添加成功");
    }
}
