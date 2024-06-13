package com.zjuse.bankingsystem.entity.loan;

import com.baomidou.mybatisplus.annotation.TableName;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;


@TableName("loan")
public class Loan {

    @JsonProperty("loan_id")
    private Long loanId;

    @JsonProperty("borrow_id")
    private Long borrowId;

    @JsonProperty("card_id")
    private Long cardId;

    @JsonProperty("officer_id")
    private int officerId;

    @JsonProperty("amount")
    private double amount;

    @JsonProperty("rate")
    private double rate;

    @JsonProperty("term")
    private int term;

    @JsonProperty("status")
    private LoanStatus status;

    @JsonProperty("date_applied")
    private LocalDate dateApplied;

    @JsonProperty("date_approved")
    private LocalDate dateApproved;


    @JsonProperty("form_id")
    private int formId;

    // Getters and Setters


    public Long getLoanId() {
        return loanId;
    }

    public void setLoanId(Long loanId) {
        this.loanId = loanId;
    }

    public Long getBorrowId() {
        return borrowId;
    }

    public void setBorrowId(Long borrowId) {
        this.borrowId = borrowId;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public int getOfficerId() {
        return officerId;
    }

    public void setOfficerId(int officerId) {
        this.officerId = officerId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public int getTerm() {
        return term;
    }

    public void setTerm(int term) {
        this.term = term;
    }

    public LoanStatus getStatus() {
        return status;
    }

    public void setStatus(LoanStatus status) {
        this.status = status;
    }

    public void setStatus(int type) {
        if(type==0) this.status=LoanStatus.application;
        else if(type==1) this.status=LoanStatus.declined;
        else if(type==2) this.status=LoanStatus.repayment;
        else if(type==3) this.status=LoanStatus.settled;
        else if(type==4) this.status=LoanStatus.overdue;
        else this.status=null;
    }

    public int getstatusnum(){
        if(this.status==LoanStatus.application) return 0;
        else if(this.status==LoanStatus.declined) return 1;
        else if(this.status==LoanStatus.repayment) return 2;
        else if(this.status==LoanStatus.settled) return 3;
        else if(this.status==LoanStatus.overdue) return 4;
        return -1;
    }

    public LocalDate getDateApplied() {
        return dateApplied;
    }

    public void setDateApplied(LocalDate dateApplied) {
        this.dateApplied = dateApplied;
    }

    public LocalDate getDateApproved() {
        return dateApproved;
    }

    public void setDateApproved(LocalDate dateApproved) {
        this.dateApproved = dateApproved;
    }

    public int getFormId() {
        return formId;
    }

    public void setFormId(int formId) {
        this.formId = formId;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loan_id=" + loanId +
                ", borrow_id=" + borrowId +
                ", card_id=" + cardId +
                ", officer_id=" + officerId +
                ", amount=" + amount +
                ", rate=" + rate +
                ", term=" + term +
                ", status=" + status +
                ", date_applied=" + dateApplied +
                ", date_approved=" + dateApproved +
                ", form_id=" + formId +
                '}';
    }
}

enum LoanStatus {
    application,
    declined,
    repayment,
    settled,
    overdue
}
