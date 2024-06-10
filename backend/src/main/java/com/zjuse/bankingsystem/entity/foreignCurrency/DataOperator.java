package com.zjuse.bankingsystem.entity.foreignCurrency;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class DataOperator {
    private String data_operator_id;
    private String username;
    private String password;
    private String email;
    private String phone_number;
    private int add_permission;
    private int delete_permission;
    private int update_permission;

    public String getData_operator_id() {
        return data_operator_id;
    }

    public void setData_operator_id(String data_operator_id) {
        this.data_operator_id = data_operator_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public int getAdd_permission() {
        return add_permission;
    }

    public void setAdd_permission(int add_permission) {
        this.add_permission = add_permission;
    }

    public int getDelete_permission() {
        return delete_permission;
    }

    public void setDelete_permission(int delete_permission) {
        this.delete_permission = delete_permission;
    }

    public int getUpdate_permission() {
        return update_permission;
    }

    public void setUpdate_permission(int update_permission) {
        this.update_permission = update_permission;
    }
}
