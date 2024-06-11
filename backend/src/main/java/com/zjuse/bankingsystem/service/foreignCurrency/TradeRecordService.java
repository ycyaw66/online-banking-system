package com.zjuse.bankingsystem.service.foreignCurrency;

import com.zjuse.bankingsystem.entity.foreignCurrency.TradeRecord;
import com.zjuse.bankingsystem.mapper.foreignCurrency.TradeRecordMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;

@Service
public class TradeRecordService {
    
    private final TradeRecordMapper tradeRecordMapper;

    @Autowired
    public TradeRecordService(TradeRecordMapper tradeRecordMapper) {
        this.tradeRecordMapper = tradeRecordMapper;
    }

    // 创建新的交易记录
    public void createTradeRecord(TradeRecord tradeRecord) {
        tradeRecordMapper.insertTradeRecord(tradeRecord);
    }

    // 根据交易 ID 获取交易记录
    public TradeRecord getTradeRecordById(String tradeId) {
        return tradeRecordMapper.findTradeRecordById(tradeId);
    }

    // 根据用户 ID 获取用户的所有交易记录
    public List<TradeRecord> getTradeRecordsByUserId(String userId) {
        return tradeRecordMapper.findTradeRecordsByUserId(userId);
    }

    public List<TradeRecord> searchTradeRecords(Map<String, Object> params) {
        return tradeRecordMapper.searchTradeRecords(params);
    }

    // 更新交易记录
    public void updateTradeRecord(TradeRecord tradeRecord) {
        tradeRecordMapper.updateTradeRecord(tradeRecord);
    }

    // 删除交易记录
    public void deleteTradeRecordById(String tradeId) {
        tradeRecordMapper.deleteTradeRecordById(tradeId);
    }
}

