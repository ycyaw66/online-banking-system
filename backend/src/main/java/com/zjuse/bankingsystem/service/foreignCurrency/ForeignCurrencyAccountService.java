package com.zjuse.bankingsystem.service.foreignCurrency;

import com.zjuse.bankingsystem.entity.foreignCurrency.ForeignCurrencyAccount;
import com.zjuse.bankingsystem.mapper.foreignCurrency.ForeignCurrencyAccountMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForeignCurrencyAccountService {

    @Autowired
    private final ForeignCurrencyAccountMapper foreignCurrencyAccountMapper;

    public ForeignCurrencyAccountService(ForeignCurrencyAccountMapper foreignCurrencyAccountMapper) {
        this.foreignCurrencyAccountMapper = foreignCurrencyAccountMapper;
    }

    public List<ForeignCurrencyAccount> findAccountsByCreditCardId(String credit_card_id) {
        return foreignCurrencyAccountMapper.findAccountsByCreditCardId(credit_card_id);
    }

    public void insertForeignCurrencyAccount(ForeignCurrencyAccount foreignCurrencyAccount) {
        foreignCurrencyAccountMapper.insertForeignCurrencyAccount(foreignCurrencyAccount);
    }

    // 根据信用卡号和外币种类判断是否存在，不存则在返回null
    public  ForeignCurrencyAccount findAccountsByCreditCardIdAndFcId(String credit_card_id, String fc_id) {
        return  foreignCurrencyAccountMapper.findAccountsByCreditCardIdAndFcId(credit_card_id, fc_id);
    }
}
