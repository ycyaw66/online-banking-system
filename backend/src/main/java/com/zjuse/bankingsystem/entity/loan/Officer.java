package com.zjuse.bankingsystem.entity.loan;

import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonProperty;

@TableName("officer")
public class Officer {

    @JsonProperty("officer_id")
    private int officerId;

    @JsonProperty("officer_name")
    private String officerName;

    @JsonProperty("phone_number")
    private String phoneNumber;

    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("permissions")
    private Permissions permissions;


    // Getters and setters
    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public String getOfficerName() {
        return officerName;
    }

    public void setOfficerName(String officerName) {
        this.officerName = officerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
                "officer_id=" + officerId +
                ", officer_name='" + officerName + '\'' +
                ", phone_number='" + phoneNumber + '\'' +
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
