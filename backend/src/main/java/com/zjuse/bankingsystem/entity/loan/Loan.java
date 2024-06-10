package com.zjuse.bankingsystem.entity.loan;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.time.LocalDate;


@TableName("loan")
public class Loan {

    @TableId(value = "loan_id", type = IdType.AUTO)
    private int loan_id;

    @TableField("borrow_id")
    private int borrow_id;

    @TableField("card_id")
    private int card_id;

    @TableField("officer_id")
    private int officer_id;

    @TableField("amount")
    private double amount;

    @TableField("rate")
    private double rate;

    @TableField("term")
    private int term;

    @TableField("status")
    private LoanStatus status;

    @TableField("date_applied")
    private LocalDate date_applied;

    @TableField("date_approved")
    private LocalDate date_approved;


    @TableField("form_id")
    private int form_id;

    // Getters and Setters


    public int getLoan_id() {
        return loan_id;
    }

    public void setLoan_id(int loan_id) {
        this.loan_id = loan_id;
    }

    public int getBorrow_id() {
        return borrow_id;
    }

    public void setBorrow_id(int borrow_id) {
        this.borrow_id = borrow_id;
    }

    public int getCard_id() {
        return card_id;
    }

    public void setCard_id(int card_id) {
        this.card_id = card_id;
    }

    public int getOfficer_id() {
        return officer_id;
    }

    public void setOfficer_id(int officer_id) {
        this.officer_id = officer_id;
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

    public LocalDate getDate_applied() {
        return date_applied;
    }

    public void setDate_applied(LocalDate date_applied) {
        this.date_applied = date_applied;
    }

    public LocalDate getDate_approved() {
        return date_approved;
    }

    public void setDate_approved(LocalDate date_approved) {
        this.date_approved = date_approved;
    }

    public int getForm_id() {
        return form_id;
    }

    public void setForm_id(int form_id) {
        this.form_id = form_id;
    }

    @Override
    public String toString() {
        return "Loan{" +
                "loan_id=" + loan_id +
                ", borrow_id=" + borrow_id +
                ", card_id=" + card_id +
                ", officer_id=" + officer_id +
                ", amount=" + amount +
                ", rate=" + rate +
                ", term=" + term +
                ", status=" + status +
                ", date_applied=" + date_applied +
                ", date_approved=" + date_approved +
                ", form_id=" + form_id +
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
