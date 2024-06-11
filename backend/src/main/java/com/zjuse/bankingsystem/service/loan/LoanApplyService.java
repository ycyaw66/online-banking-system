package com.zjuse.bankingsystem.service.loan;

import com.zjuse.bankingsystem.entity.loan.Loan;
import com.zjuse.bankingsystem.mapper.loan.AutoLoanMapper;
import com.zjuse.bankingsystem.mapper.loan.IdgetMapper;
import com.zjuse.bankingsystem.mapper.loan.LoanApplyMapper;

import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class LoanApplyService {

    @Autowired
    private LoanApplyMapper loanMapper;
    @Autowired
    private IdgetMapper idgetMapper;
    @Autowired
    private AutoLoanMapper  autoloanMapper;

    public int insertloan(Loan loan){
        return  loanMapper.insert(loan);
    }
    public int autoloan(Loan loan){
        return autoloanMapper.insert(loan);
    }
    public int getofficerid(String permission){
        return  idgetMapper.idrandomget(permission);
    }
}