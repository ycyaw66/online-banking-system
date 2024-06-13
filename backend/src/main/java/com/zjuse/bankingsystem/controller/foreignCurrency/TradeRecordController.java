package com.zjuse.bankingsystem.controller.foreignCurrency;

import com.zjuse.bankingsystem.entity.foreignCurrency.TradeRecord;
import com.zjuse.bankingsystem.utils.RespResult;
import com.zjuse.bankingsystem.service.foreignCurrency.TradeRecordService;
import com.zjuse.bankingsystem.security.service.CurrentUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin // 允许的前端地址
@RestController
@RequestMapping(value = "/fc/trade/history")
public class TradeRecordController {

    @Autowired
    private TradeRecordService tradeRecordService;

    @Autowired
    private CurrentUserService currentUserService;

    @GetMapping("/query")
    public RespResult getTradeRecordsByUserId() {
        // 调用Service层方法获取所有交易记录
        // 返回交易记录列表给前端
        String userId = currentUserService.getCurrentUserId().payload.toString();
        List<TradeRecord> tradeRecords = tradeRecordService.getTradeRecordsByUserId(userId);
        if(tradeRecords.isEmpty())
            return RespResult.fail("No trade record");
        return RespResult.success(tradeRecords);
    }

    @PostMapping("/search")
    public RespResult searchTradeRecords(@RequestBody Map<String, Object> params) {
        String userId = currentUserService.getCurrentUserId().payload.toString();
        params.put("userId", userId);
        List<TradeRecord> trade = tradeRecordService.searchTradeRecords(params);
        if(trade.isEmpty())
            return RespResult.fail("No trade record");
        return RespResult.success(trade);
    }
}

