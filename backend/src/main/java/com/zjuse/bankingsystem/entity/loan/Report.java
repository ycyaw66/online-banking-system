package com.zjuse.bankingsystem.entity.loan;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;

public class Report {
    @JsonProperty("report_id")
    private int reportId;

    @JsonProperty("user_id")
    private Long userId;

    @JsonProperty("creditLimit")
    private double creditLimit;

    @JsonProperty("date")
    private LocalDate date;

    @JsonProperty("credit_score")
    private double creditScore;

    // Getters and Setters

    public int getReportId() {
        return reportId;
    }

    public void setReportId(int reportId) {
        this.reportId = reportId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public double getCreditLimit() {
        return creditLimit;
    }

    public void setCreditLimit(double creditLimit) {
        this.creditLimit = creditLimit;
    }

    public void setCreditScore(double creditScore) {
        this.creditScore = creditScore;
    }


    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }
}
