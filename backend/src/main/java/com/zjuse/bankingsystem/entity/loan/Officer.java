package com.zjuse.bankingsystem.entity.loan;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("officer")
public class Officer {

    @TableId(value = "officer_id", type = IdType.AUTO)
    private int officer_id;

    @TableField("officer_name")
    private String officer_name;

    @TableField("phone_number")
    private String phone_number;

    @TableField("username")
    private String username;

    @TableField("password")
    private String password;

    @TableField("permissions")
    private Permissions permissions;


    // Getters and setters
    public int getOfficer_id() {
        return officer_id;
    }

    public void setOfficer_id(int officer_id) {
        this.officer_id = officer_id;
    }

    public String getOfficer_name() {
        return officer_name;
    }

    public void setOfficer_name(String officer_name) {
        this.officer_name = officer_name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
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

    public Permissions getPermissions() {
        return permissions;
    }

    public void setPermissions(Permissions permissions) {
        this.permissions = permissions;
    }

    @Override
    public String toString() {
        return "Officer{" +
                "officer_id=" + officer_id +
                ", officer_name='" + officer_name + '\'' +
                ", phone_number='" + phone_number + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", permissions=" + permissions +
                '}';
    }
}

enum Permissions {
    small,
    large
}
