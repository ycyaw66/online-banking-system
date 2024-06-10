package com.zjuse.bankingsystem.service.foreignCurrency.implementation;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.zjuse.bankingsystem.entity.foreignCurrency.ForeignCurrency;
import com.zjuse.bankingsystem.entity.foreignCurrency.ForeignCurrencyRate;
import com.zjuse.bankingsystem.mapper.foreignCurrency.CurrencyManagerMapper;
import com.zjuse.bankingsystem.model.Currency;
import com.zjuse.bankingsystem.service.foreignCurrency.CurrencyManagerService;

@Service
public class CurrencyManagerServiceImp implements CurrencyManagerService {

    @Autowired
    private CurrencyManagerMapper currencyManagerMapper;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean addCurrencyRate(double rate, String currency_name, String data_operator_id) throws Exception {
        ForeignCurrency fc = currencyManagerMapper.selectForeignCurrencyExactly(currency_name);
        // currencyManagerMapper.addCurrencyRate("10391810338014", rate);
        if(fc==null)
            throw new Exception("no such currency");
        if(currencyManagerMapper.addCurrencyRate(fc.getFc_id(), rate) == 1)
            return true;
        throw new Exception("unkown error");
    }

    @Override
    public List<Currency> selectAllCurrency() {
        List<ForeignCurrency> fc = currencyManagerMapper.selectAllForeignCurrency();
        if(fc == null)
            return null;
        List<Currency> currency = new ArrayList<Currency>();
        for(ForeignCurrency f : fc){
            Currency c = selectSingleCurrency(f.getFc_id(), f.getFc_name());
            currency.add(c);
        }
        return currency;
    }

    @Override
    public List<Currency> selectCurrency(String currency_name) {
        List<ForeignCurrency> fc = currencyManagerMapper.selectForeignCurrency(currency_name);
        if(fc == null)
            return null;
        List<Currency> currency = new ArrayList<Currency>();
        for(ForeignCurrency f : fc){
            Currency c = selectSingleCurrency(f.getFc_id(), f.getFc_name());
            currency.add(c);
        }
        return currency;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public boolean updateCurrencyRate(LocalDateTime date, double rate, String currency_name, String data_operator_id) throws Exception {
        ForeignCurrency fc = currencyManagerMapper.selectForeignCurrencyExactly(currency_name);
        if(fc==null)
            throw new Exception("no such currency");
        if(currencyManagerMapper.updateCurrencyRate(fc.getFc_id(), rate, date) == 1)
            return true;
        throw new Exception("error date");
    }

    private Currency selectSingleCurrency(String fc_id, String name){
        List<ForeignCurrencyRate> fcr = currencyManagerMapper.selectForeignCurrencyRate(fc_id);
        return new Currency(fc_id, name, fcr);
    }

    @Override
    public double getCurrencyRate(String currency_name, LocalDateTime date) {
        return currencyManagerMapper.selectForeignCurrencyRateExactly(currency_name, date).getFc_rate();
    }

    @Override
    public double selectRecentRate(String id) {
        List<ForeignCurrencyRate> fcr = currencyManagerMapper.selectForeignCurrencyRate(id);
        if(fcr.isEmpty())
            throw new RuntimeException("no such currency");
        int size = fcr.size();
        return fcr.get(size-1).getFc_rate();
    }

}
