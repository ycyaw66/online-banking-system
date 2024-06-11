package com.zjuse.bankingsystem.service.foreignCurrency;

import com.zjuse.bankingsystem.entity.foreignCurrency.HistoryOperationRecord;

public interface HistoryOperationRecordService {

    void createHistoryOperationRecord(HistoryOperationRecord historyOperationRecord) throws Exception;

}
