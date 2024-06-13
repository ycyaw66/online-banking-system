package com.zjuse.bankingsystem.controller.loan;

import com.zjuse.bankingsystem.entity.loan.Report;
import com.zjuse.bankingsystem.entity.loan.Loan;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.loan.CreditReportService;
import com.zjuse.bankingsystem.service.loan.LoanApprovalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin
public class CreditReportController {

    @Autowired
    private CreditReportService creditReportService;
    @Autowired
    private CurrentUserService currentUserService;
    @Autowired
    private LoanApprovalService loanApprovalService;

    @PostMapping("/generate-credit-report")
    public Map<String, Object> generateCreditReport() {
        // 获取用户ID
        Long userId = (Long)currentUserService.getCurrentUserId().payload;                                                     //need  user_id
        System.out.println(userId);
        // 计算信用评分
        double credit_score = creditReportService.calculateCreditScore(userId);
        System.out.println(credit_score);
        //  计算信用额度
        double credit_limit = creditReportService.calculateCreditLimit(userId);
        System.out.println(credit_limit);
        // 插入报告
        Report report = new Report();
        report.setUserId(userId);
        report.setCreditScore(credit_score);
        report.setCreditLimit(credit_limit);
        report.setDate(LocalDate.now());
        creditReportService.insertReport(report);

        // 返回信用评分和信用额度
        return Map.of("creditScore", credit_score, "creditLimit", credit_limit);
    }

    @PutMapping("/get-credit")
    public Map<String, Object> getCreditReport(@RequestBody Map<String, Integer> request) {
        int formId = request.get("form_id");
        System.out.println(formId);
        Loan loan = loanApprovalService.searchLoans(formId);
        // 获取用户ID
        long userId = loan.getBorrowId();
        System.out.println(userId);
        // 计算信用评分
        double creditScore = creditReportService.calculateCreditScore(userId);
        System.out.println(creditScore);
        //  计算信用额度
        double creditLimit = creditReportService.calculateCreditLimit(userId);
        System.out.println(creditLimit);
        // 插入报告
        Report report = new Report();
        report.setUserId(userId);
        report.setCreditScore(creditScore);
        report.setCreditLimit(creditLimit);
        report.setDate(LocalDate.now());
        creditReportService.insertReport(report);

        // 返回信用评分和信用额度
        return Map.of("creditScore", creditScore, "creditLimit", creditLimit);
    }
}