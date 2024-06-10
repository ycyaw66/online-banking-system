package com.zjuse.bankingsystem.service.foreignCurrency;

import com.zjuse.bankingsystem.entity.foreignCurrency.DataOperator;
import com.zjuse.bankingsystem.model.DataOperatorInfo;
import com.zjuse.bankingsystem.model.UpdateInfo;

public interface DataOperatorService {

    DataOperatorInfo selectDataOperatorByUsernameAndPassword(String username, String password) throws Exception;

    DataOperatorInfo selectDataOperatorById(String id) throws Exception;

    void register(DataOperator entity) throws Exception;

    void detailedInformation(DataOperator entity) throws Exception;

    void updateDataOperatorPassword(String data_operator_id, String new_password, String old_password) throws Exception;

    void deleteDataOperator(String data_operator_id) throws Exception;

    void updateInfomation(UpdateInfo updateInfo) throws Exception;
    
}
