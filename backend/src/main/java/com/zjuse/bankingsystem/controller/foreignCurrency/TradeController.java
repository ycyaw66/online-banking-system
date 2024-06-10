package com.zjuse.bankingsystem.controller.foreignCurrency;

import com.zjuse.bankingsystem.entity.foreignCurrency.TradeRecord;
import com.zjuse.bankingsystem.service.foreignCurrency.TradeService;
import com.zjuse.bankingsystem.utils.RespResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * TradeController提供外汇交易的API接口。
 */
@RestController
@CrossOrigin
@RequestMapping("/fc/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    /**
     * 执行外汇交易的接口。
     *
     * @return 交易结果消息
     */
    @PostMapping("/execute")
    public RespResult executeTrade(
            @RequestBody TradeRecord record) {
        // TODO: Check record.getUserId() 与 record.getCreditCardId() 是否合法
        return tradeService.executeTrade(record);
    }
}
