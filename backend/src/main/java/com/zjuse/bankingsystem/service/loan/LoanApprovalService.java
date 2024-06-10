package com.zjuse.bankingsystem.service.loan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.zjuse.bankingsystem.entity.loan.Loan;
import com.zjuse.bankingsystem.mapper.loan.LoanMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LoanApprovalService {

    @Autowired
    private LoanMapper loanMapper;

    public IPage<Loan> getLoan(Page<Loan> loanPage, int officer_id){
        return loanMapper.selectPage(loanPage, new QueryWrapper<Loan>().eq("status", "application").eq("officer_id", officer_id));
    }

    public int updateLoan(UpdateWrapper<Loan> updateWrapper){
        return loanMapper.update(updateWrapper);
    }

    public IPage<Loan> getApprovalHistory(Page<Loan> loanPage, int officer_id){
        return loanMapper.selectPage(loanPage, new QueryWrapper<Loan>().ne("status", "application").eq("officer_id",officer_id));
    }

    public Loan searchLoans(int form_id) {
        return loanMapper.selectOne(new QueryWrapper<Loan>().eq("form_id", form_id));
    }

    public int getCardId(int loan_id) {return loanMapper.getCardId(loan_id);}
    public double getAmount(int loan_id) {return loanMapper.getAmount(loan_id);}

    //为银行卡号为card_id的账户增加余额，量为amout
    public void AddBalance(int card_id, double amount) {

    }
}
