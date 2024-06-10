package com.zjuse.bankingsystem.service.foreignCurrency.implementation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zjuse.bankingsystem.entity.foreignCurrency.HistoryOperationRecord;
import com.zjuse.bankingsystem.mapper.foreignCurrency.HistoryOperationRecordMapper;
import com.zjuse.bankingsystem.service.foreignCurrency.HistoryOperationRecordService;

@Service
public class HistoryOperationRecordServiceImp implements HistoryOperationRecordService {

    @Autowired
    HistoryOperationRecordMapper historyOperationRecordMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void createHistoryOperationRecord(HistoryOperationRecord historyOperationRecord) throws Exception{
        if(1!=historyOperationRecordMapper.addHistoryOperationRecord(historyOperationRecord))
            throw new Exception("添加历史操作记录失败");
    }
}
