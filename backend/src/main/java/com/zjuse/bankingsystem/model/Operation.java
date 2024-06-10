package com.zjuse.bankingsystem.model;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zjuse.bankingsystem.entity.foreignCurrency.HistoryOperationRecord;
import com.zjuse.bankingsystem.service.foreignCurrency.CurrencyManagerService;

public class Operation{
    String fc_name;
    double rate;
    String data_operator_id;
    int opid;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    LocalDateTime dest_date;

    public HistoryOperationRecord toHistoryOperationRecord(CurrencyManagerService currencyManagerService){
        String record_id = LocalDateTime.now().toString();
        if(record_id.length() > 20)
            record_id = record_id.substring(0, 20);
        String data_operator_id = this.data_operator_id;
        String fc_id = currencyManagerService.selectCurrency(fc_name).get(0).getFc_id();
        String operation = opid == 0 ? "add" : "update";
        double old_rate = opid == 0 ? 0 : currencyManagerService.getCurrencyRate(fc_name, dest_date);
        double new_rate = this.rate;
        LocalDateTime operation_time = LocalDateTime.now();
        LocalDateTime dest_date = this.dest_date;
        return new HistoryOperationRecord(record_id, data_operator_id, fc_id, operation, old_rate, new_rate, dest_date, operation_time);
    }

    public Operation(){
        opid = 0;
        dest_date = null;
        fc_name = "";
        rate = 0;
    }

    public String getFc_name() {
        return fc_name;
    }

    public void setFc_name(String fc_name) {
        this.fc_name = fc_name;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public String getData_operator_id() {
        return data_operator_id;
    }

    public void setData_operator_id(String data_operator_id) {
        this.data_operator_id = data_operator_id;
    }

    public int getOpid() {
        return opid;
    }

    public void setOpid(int opid) {
        this.opid = opid;
    }

    public LocalDateTime getDest_date() {
        return dest_date;
    }

    public void setDest_date(LocalDateTime dest_date) {
        this.dest_date = dest_date;
    }

    public enum OPERATION{
        ADD(0), 
        UPDATE(1), 
        DELETE(2);

        private final int value;

        OPERATION(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
