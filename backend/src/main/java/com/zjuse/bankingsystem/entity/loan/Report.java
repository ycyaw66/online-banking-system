package com.zjuse.bankingsystem.entity.loan;

import java.time.LocalDate;

public class Report {
    private int report_id;
    private int user_id;
    private double creditLimit;
    private LocalDate date;
    private double creditScore;

    // Getters and Setters

    public int getReport_id() {
        return report_id;
    }

    public void setReport_id(int report_id) {
        this.report_id = report_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public void setCreditScore(double creditScore) {this.creditScore = creditScore;}

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
