package com.zjuse.bankingsystem.controller.foreignCurrency;

import com.zjuse.bankingsystem.entity.foreignCurrency.TradeRecord;
import com.zjuse.bankingsystem.service.foreignCurrency.TradeRecordService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@CrossOrigin // 允许的前端地址
@RestController
@RequestMapping(value = "/fc/trade/history")
public class TradeRecordController {

    @Autowired
    private final TradeRecordService tradeRecordService;

    public TradeRecordController(TradeRecordService tradeRecordService) {
        this.tradeRecordService = tradeRecordService;
    }

    @GetMapping("/{userId}")
    public List<TradeRecord> getTradeRecordsByUserId(@PathVariable String userId) {
        // 调用Service层方法获取所有交易记录
        // 返回交易记录列表给前端
        return tradeRecordService.getTradeRecordsByUserId(userId);
    }

    @PostMapping("/search")
    public List<TradeRecord> searchTradeRecords(@RequestBody Map<String, Object> params) {
        return tradeRecordService.searchTradeRecords(params);
    }
}

