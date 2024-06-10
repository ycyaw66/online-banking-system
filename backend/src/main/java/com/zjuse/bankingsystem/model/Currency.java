package com.zjuse.bankingsystem.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.zjuse.bankingsystem.entity.foreignCurrency.ForeignCurrencyRate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

public class Currency {
    @AllArgsConstructor
    @Getter
    @Setter
    public class CurrencyRate{
        private LocalDateTime history_date;
        private double fc_rate;
    }

    private String fc_id;
    private String currency_name;
    private double fc_rate;
    private List<CurrencyRate> currency_rate;

    public String getCurrency_name() {
        return currency_name;
    }

    public void setCurrency_name(String currency_name) {
        this.currency_name = currency_name;
    }

    public List<CurrencyRate> getCurrency_rate() {
        return currency_rate;
    }

    public void setCurrency_rate(List<CurrencyRate> currency_rate) {
        this.currency_rate = currency_rate;
    }

    public String getFc_id() {
        return fc_id;
    }

    public void setFc_id(String fc_id) {
        this.fc_id = fc_id;
    }

    public double getFc_rate() {
        return fc_rate;
    }

    public Currency(String fc_id, String currency_name, List<ForeignCurrencyRate> fc_rates) {
        this.fc_id = fc_id;
        this.currency_name = currency_name;
        this.currency_rate = new ArrayList<>();
        this.fc_rate = 0;
        if(fc_rates != null&&fc_rates.size()!=0){
            for(ForeignCurrencyRate rate : fc_rates)
                this.currency_rate.add(new CurrencyRate(rate.getFc_date(), rate.getFc_rate()));
            this.fc_rate = this.currency_rate.get(this.currency_rate.size() - 1).getFc_rate();
        }
    }

}
