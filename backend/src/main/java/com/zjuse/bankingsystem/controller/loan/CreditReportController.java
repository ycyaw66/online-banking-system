package com.zjuse.bankingsystem.controller.loan;

import com.zjuse.bankingsystem.entity.loan.Report;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.loan.CreditReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.Map;

@RestController
@CrossOrigin
public class CreditReportController {

    @Autowired
    private CreditReportService creditReportService;
    @Autowired
    private CurrentUserService currentUserService; 

    @PostMapping("/generate-credit-report")
    public Map<String, Object> generateCreditReport(@RequestParam("password") String password) {
        // 获取用户ID
        Long userId = (Long)currentUserService.getCurrentUserId().payload;                                                     //need  user_id
        System.out.println(userId);
        // 计算信用评分
        double credit_score = creditReportService.calculateCreditScore(userId);
        System.out.println(credit_score);
        //  计算信用额度
        double credit_limit = creditReportService.calculateCreditLimit(userId, password);
        System.out.println(credit_limit);
        // 插入报告
        Report report = new Report();
        report.setUser_id(userId);
        report.setCreditScore(credit_score);
        report.setCreditLimit(credit_limit);
        report.setDate(LocalDate.now());
        creditReportService.insertReport(report);

        // 返回信用评分和信用额度
        return Map.of("creditScore", credit_score, "creditLimit", credit_limit);
    }
}