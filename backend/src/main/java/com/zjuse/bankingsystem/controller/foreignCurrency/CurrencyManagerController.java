package com.zjuse.bankingsystem.controller.foreignCurrency;

import org.springframework.web.bind.annotation.RestController;

import com.zjuse.bankingsystem.entity.foreignCurrency.HistoryOperationRecord;
import com.zjuse.bankingsystem.model.Currency;
import com.zjuse.bankingsystem.model.DataOperatorInfo;
import com.zjuse.bankingsystem.model.Operation;
import com.zjuse.bankingsystem.service.foreignCurrency.CurrencyManagerService;
import com.zjuse.bankingsystem.service.foreignCurrency.DataOperatorService;
import com.zjuse.bankingsystem.service.foreignCurrency.HistoryOperationRecordService;
import com.zjuse.bankingsystem.utils.ApiResult;
import com.zjuse.bankingsystem.utils.RespResult;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@CrossOrigin
@RequestMapping("/fc/currency")
public class CurrencyManagerController {

    @Autowired
    private CurrencyManagerService currencyManagerService;

    @Autowired
    private HistoryOperationRecordService historyOperationRecordService;

    @Autowired
    private DataOperatorService dataOperatorService;

    @GetMapping("/{fc_name}")
    public RespResult searchCurrency(@PathVariable String fc_name) {
        List<Currency> selected_currency;
        ApiResult result = new ApiResult(false, "No currency");
        if(fc_name.equals("all"))
            selected_currency = currencyManagerService.selectAllCurrency();
        else
            selected_currency = currencyManagerService.selectCurrency(fc_name);
        result.payload = selected_currency;
        if(selected_currency.size() != 0)
        {
            result.ok = true;
            result.message = "Success";
        }
        else{
            result.message = "No currency";
        }
        return new RespResult(result.ok?0:1, result.message, result.payload);
    }

    @RequestMapping("/operation")
    public RespResult operatorCurrency(@RequestBody Operation operation) {
        ApiResult result = new ApiResult(false, "fail to ");
        try{
            DataOperatorInfo dataOperator = dataOperatorService.selectDataOperatorById(operation.getData_operator_id());
            if(operation.getOpid() == Operation.OPERATION.ADD.getValue()){
                if(!dataOperator.getAdd_permission())
                    throw new Exception("No permission");
                if(currencyManagerService.addCurrencyRate(operation.getRate(), operation.getFc_name(), operation.getData_operator_id())) {
                    result.ok = true;
                    result.message = "Success";
                }   
            }
            else if(operation.getOpid() == Operation.OPERATION.UPDATE.getValue()){
                if(!dataOperator.getAdd_permission())
                    throw new Exception("No permission");
                if(currencyManagerService.updateCurrencyRate(operation.getDest_date(),operation.getRate(), operation.getFc_name(), operation.getData_operator_id())) {
                    result.ok = true;
                    result.message = "Success";
                }
            }else{
                result.message = "error operation";
            }
        }catch(Exception e){
            return RespResult.fail( e.getMessage());
        }
        HistoryOperationRecord historyOperationRecord = operation.toHistoryOperationRecord(currencyManagerService);
        if(result.ok){
            try{
                historyOperationRecordService.createHistoryOperationRecord(historyOperationRecord);
            }catch(Exception e){
                return RespResult.fail( e.getMessage());
            }
        }
        return RespResult.success(result.payload);
    }

}
