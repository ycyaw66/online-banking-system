package com.zjuse.bankingsystem.service.loan;

import com.zjuse.bankingsystem.entity.loan.Loan;
import com.zjuse.bankingsystem.entity.loan.Report;
import com.zjuse.bankingsystem.mapper.loan.LoanQueryMapper;
import com.zjuse.bankingsystem.mapper.loan.ReportMapper;
import com.zjuse.bankingsystem.service.user.UserAndCardService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class CreditReportService {

    @Autowired
    private LoanQueryMapper loanQueryMapper;

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private UserAndCardService userAndCardService; 

    public double calculateCreditScore(Long userId) {
        // 查询用户的贷款记录
        List<Loan> loans = loanQueryMapper.getLoansByUserId(userId);

        // 计算信用评分
        double creditScore = 100.0;
        for (Loan loan : loans) {
            if (loan.getstatusnum()==3) {
                creditScore += loan.getAmount() / 5000.0;
            } else if (loan.getstatusnum()==4) {
                creditScore -= loan.getAmount() / 10000.0;
            }
        }

        // 限制信用评分在 100 到 200 之间
        creditScore = Math.max(100.0, Math.min(creditScore, 200.0));

        return creditScore;
    }

    public double calculateCreditLimit(Long userId, String password) {

                                                                            // 需要三个月内的流水
        double moneyin = ((BigDecimal) userAndCardService.getMoneyStream(userId).payload).doubleValue();
        double innum;
        //计算流水系数
        if(moneyin<=10000) innum=0.2;
        else if(moneyin<=20000) innum=0.25;
        else if(moneyin<=50000) innum=0.3;
        else if(moneyin<=100000) innum=0.35;
        else innum=0.4;
        
                                                       
                                                                            //需要用户所有卡的余额综合
        double money = ((BigDecimal) userAndCardService.getBalance(userId, password).payload).doubleValue();  //用户总余额
        double loan_money=0;      //用户未还的款
        List<Loan> loans = loanQueryMapper.getLoansByUserId(userId);
        for (Loan loan : loans) {
            if (loan.getstatusnum()==4) {
                loan_money+=loan.getAmount();
            }
        }

        // 计算信用评分
        double creditScore = 100.0;
        for (Loan loan : loans) {
            if (loan.getstatusnum()==3) {
                creditScore += loan.getAmount() / 5000.0;
            } else if (loan.getstatusnum()==4) {
                creditScore -= loan.getAmount() / 10000.0;
            }
        }
        creditScore = Math.max(100.0, Math.min(creditScore, 200.0));



        double credit_limit=money*innum + 10000*creditScore-loan_money;
        return credit_limit;
    }

    public void insertReport(Report report) {
        reportMapper.insert(report);
    }
}