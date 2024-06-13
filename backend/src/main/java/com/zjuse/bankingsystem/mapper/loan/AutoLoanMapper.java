package com.zjuse.bankingsystem.mapper.loan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.loan.Loan;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;

@Mapper
public interface AutoLoanMapper extends BaseMapper<Loan> {
    @Insert("INSERT INTO loan (borrow_id,card_id,officer_id,amount,rate,term,status,date_applied,date_approved,form_id) " +
            "VALUES (#{borrowId}, #{cardId}, null, #{amount}, #{rate}, #{term}, #{status}, #{dateApplied}, #{dateApproved}, #{formId})")
    @Options(useGeneratedKeys = true, keyProperty = "loanId")
    int insert(Loan loan);
}
