package com.zjuse.bankingsystem.entity.foreignCurrency;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ForeignCurrencyRate {
    private String fc_id;
    private LocalDateTime fc_date;
    private double fc_rate;

    public String getFc_id() {
        return fc_id;
    }

    public void setFc_id(String fc_id) {
        this.fc_id = fc_id;
    }

    public LocalDateTime getFc_date() {
        return fc_date;
    }

    public void setFc_date(LocalDateTime fc_date) {
        this.fc_date = fc_date;
    }

    public double getFc_rate() {
        return fc_rate;
    }

    public void setFc_rate(double fc_rate) {
        this.fc_rate = fc_rate;
    }
}
