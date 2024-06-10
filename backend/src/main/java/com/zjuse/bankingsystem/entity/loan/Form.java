package com.zjuse.bankingsystem.entity.loan;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

@TableName("form")
public class Form {

    @TableId(value = "form_id", type = IdType.AUTO)
    private int form_id;

    @TableField("user_name")
    private String user_name;

    @TableField("id_number")
    private String id_number;

    @TableField("gender")
    private Gender gender;

    @TableField("emotion")
    private Emotion emotion;

    @TableField("income")
    private int income;

    @TableField("address")
    private String address;

    @TableField("phone_number")
    private String phone_number;

    @TableField("email")
    private String email;

    @TableField("education")
    private String education;

    @TableField("purpose")
    private String purpose;

    @TableField("statement")
    private String statement;

    // Getters and setters

    public int getForm_id() {
        return form_id;
    }

    public void setForm_id(int form_id) {
        this.form_id = form_id;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getId_number() {
        return id_number;
    }

    public void setId_number(String id_number) {
        this.id_number = id_number;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public Emotion getEmotion() {
        return emotion;
    }

    public void setEmotion(Emotion emotion) {
        this.emotion = emotion;
    }

    public int getIncome() {
        return income;
    }

    public void setIncome(int income) {
        this.income = income;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }
}

enum Gender {
    male,
    female
}

enum Emotion {
    single,
    married
}
