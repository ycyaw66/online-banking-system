package com.zjuse.bankingsystem.controller.loan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjuse.bankingsystem.entity.loan.Loan;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.loan.LoanQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class QueryLoanController {

    @Autowired
    private LoanQueryService loanQueryService;
    @Autowired
    private CurrentUserService currentUserService; 

    @PostMapping("/loan-history")
    public Map<String, Object> getLoanHistory(@RequestBody LoanQueryRequest filter,
                                              @RequestParam(defaultValue = "1") int page,
                                              @RequestParam(defaultValue = "10") int size) {
        QueryWrapper<Loan> queryWrapper = new QueryWrapper<>();
                                                                                 
        int userId = (int) currentUserService.getCurrentUserId().payload;
        queryWrapper.eq("borrower_id", userId);
        
        if (filter.getAmount() != null) {
            queryWrapper.eq("amount", filter.getAmount());
        }
        if (filter.getStartDate() != null && filter.getEndDate() != null) {
            queryWrapper.between("date_applied", filter.getStartDate(), filter.getEndDate());
        }
        if (filter.getStatus() != null) {
            queryWrapper.eq("status", filter.getStatus());
        }
        if (filter.getRate() != null) {
            queryWrapper.eq("rate", filter.getRate());
        }

        // �����������
        if (filter.getSortCondition() != null && !filter.getSortCondition().isEmpty()) {
            queryWrapper.orderByDesc(filter.getSortCondition());
        }

        // ��ҳ��ѯ
        Page<Loan> loanPage = new Page<>(page, size);
        Page<Loan> resultPage = loanQueryService.query(loanPage, queryWrapper);

        // �������ؽ��
        Map<String, Object> response = new HashMap<>();
        response.put("total", resultPage.getTotal());
        response.put("loans", resultPage.getRecords());

        return response;
    }
    

    // �ڲ������ڽ���ǰ�˴��ݵĲ�ѯ����
    static class LoanQueryRequest {
        private Double amount;
        private LoanStatus status;
        private Double rate;
        private LocalDate date_applied;
        private LocalDate date_approved;
        private String sortCondition;

        public Double getAmount() {return amount;}
        public Double getRate() {return rate;}
        public LocalDate getStartDate() {return date_applied;}
        public LocalDate getEndDate() {return date_approved;}
        public String getSortCondition() {return sortCondition;}
        public LoanStatus getStatus() {return status;}
        enum LoanStatus {
            application,
            declined,
            repayment,
            settled,
            overdue
        }
    }
}
