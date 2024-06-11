package com.zjuse.bankingsystem.model;

import com.zjuse.bankingsystem.entity.foreignCurrency.DataOperator;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class DataOperatorInfo {
    private String data_operator_id;
    private String username;
    private String email;
    private String phone_number;
    private Boolean add_permission;
    private Boolean delete_permission;
    private Boolean update_permission;

    public DataOperatorInfo(DataOperator obj) {
        this.data_operator_id = obj.getData_operator_id();
        this.username = obj.getUsername();
        this.email = obj.getEmail();
        this.phone_number = obj.getPhone_number();
        this.add_permission = obj.getAdd_permission()==1;
        this.delete_permission = obj.getDelete_permission()==1;
        this.update_permission = obj.getUpdate_permission()==1;
    }
}
