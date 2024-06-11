package com.zjuse.bankingsystem.entity.foreignCurrency;

import lombok.Data;

@Data
public class ForeignCurrency {
    private String fc_id;
    private String fc_name;

    public ForeignCurrency(String fc_id, String fc_name) {
        this.fc_id = fc_id;
        this.fc_name = fc_name;
    }

    public String getFc_id() {
        return fc_id;
    }

    public void setFc_id(String fc_id) {
        this.fc_id = fc_id;
    }

    public String getFc_name() {
        return fc_name;
    }

    public void setFc_name(String fc_name) {
        this.fc_name = fc_name;
    }

}
