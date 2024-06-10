package com.zjuse.bankingsystem.controller.loan;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjuse.bankingsystem.service.loan.LoanApprovalService;
import com.zjuse.bankingsystem.service.loan.OfficerLoginService;
import com.zjuse.bankingsystem.service.loan.AmountService;
import com.zjuse.bankingsystem.service.loan.CreditReportService;
import com.zjuse.bankingsystem.service.loan.LoanApplyService;
import com.zjuse.bankingsystem.entity.loan.Loan;
import com.zjuse.bankingsystem.entity.loan.Officer;
import com.zjuse.bankingsystem.security.service.CurrentUserService;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin
public class LoanController {

    @Autowired
    private LoanApplyService loanApplyService;
    @Autowired
    private CreditReportService creditReportService;
    @Autowired
    private AmountService amountService;

    @PostMapping("/add-loan")
    public Map<String, Object> insertLoan(@RequestBody Loan loan) {

        int result;
        Map<String, Object> response = new HashMap<>();

        //get borrower_id(user_id) from outside                                            not implement
        loan.setBorrow_id(1);

        //set date
        loan.setDate_applied(LocalDate.now());


        Double credit= creditReportService.calculateCreditLimit(loan.getBorrow_id());

        String permission;double rate;
        if(loan.getAmount()>100000) {permission="large";rate=0.03;}
        else {permission="small";rate=0.01;}
        rate+=0.02*(1-credit/loan.getAmount());
        loan.setRate(rate);

        //artificial
        if(loan.getAmount()>credit){
            loan.setStatus(0);
            //get random suitable officer_id from officer_tanble
            int id=loanApplyService.getofficerid(permission);
            loan.setOfficer_id(id);
            result=loanApplyService.insertloan(loan);
        }
        //automic
        else{
            double money=amountService.getamount(loan.getCard_id());                     // update amount
            amountService.changeamount(loan.getCard_id(), money);
            loan.setStatus(2);
            loan.setDate_approved(LocalDate.now());
            result=loanApplyService.autoloan(loan);
        }

        if (result > 0) {
            response.put("message", "Loan created successfully!");
            response.put("loan_id", loan.getLoan_id());
        } else {
            response.put("message", "Failed to create loan.");
        }
        return response;
    }


    @Autowired
    private OfficerLoginService loginService;
    @Autowired
    private LoanApprovalService loanApprovalService;
    @Autowired
    private CurrentUserService currentUserService;

    @GetMapping("/get-loans")
    public IPage<Loan> getLoans(@RequestParam int page, @RequestParam int pageSize, HttpServletRequest request) {
        String officerUsername;
        officerUsername = (String)currentUserService.getCurrentUsername().payload;
        Officer officer = loginService.findOfficerByUsername(officerUsername);

        int officer_id = officer.getOfficer_id();
        Page<Loan> loanPage = new Page<>(page, pageSize);
        return loanApprovalService.getLoan(loanPage, officer_id);
    }

    @PutMapping("/approve-loan/{id}")
    public String approveLoan(@PathVariable("id") int loan_id) {
        UpdateWrapper<Loan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("loan_id", loan_id).set("status", "repayment").set("date_approved", LocalDate.now());
        loanApprovalService.AddBalance(loanApprovalService.getCardId(loan_id), loanApprovalService.getAmount(loan_id));
        int result = loanApprovalService.updateLoan(updateWrapper);
        if (result > 0) {
            return "Loan approved successfully!";
        } else {
            return "Failed to approve loan.";
        }
    }

    @PutMapping("/reject-loan/{id}")
    public String rejectLoan(@PathVariable("id") int loan_id) {
        UpdateWrapper<Loan> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("loan_id", loan_id).set("status", "declined").set("date_approved", LocalDate.now());
        int result = loanApprovalService.updateLoan(updateWrapper);
        if (result > 0) {
            return "Loan rejected successfully!";
        } else {
            return "Failed to reject loan.";
        }
    }

    @GetMapping("/get-approvals")
    public IPage<Loan> getApprovals(@RequestParam int page, @RequestParam int pageSize, HttpServletRequest request) {
        String officerUsername;
        officerUsername = (String)currentUserService.getCurrentUsername().payload;
        Officer officer = loginService.findOfficerByUsername(officerUsername);

        int officer_id = officer.getOfficer_id();
        Page<Loan> loanPage = new Page<>(page, pageSize);
        return loanApprovalService.getApprovalHistory(loanPage, officer_id);
    }

    @GetMapping("/search-loans/{form_id}")
    public Loan searchLoans(@PathVariable("form_id") int form_id) {
        return loanApprovalService.searchLoans(form_id);
    }
}