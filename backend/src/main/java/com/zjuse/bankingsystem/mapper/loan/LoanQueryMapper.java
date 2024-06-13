package com.zjuse.bankingsystem.mapper.loan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.loan.Loan;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface LoanQueryMapper extends BaseMapper<Loan> {

    @Select("SELECT amount FROM loan WHERE loan_id = #{loanId}")
    double getAmount(@Param("loanId") int loan_id);

    @Select("SELECT rate FROM loan WHERE loan_id = #{loanId}")
    double getrate(@Param("loanId") int loan_id);

    @Update("UPDATE loan SET status = 'repayment' WHERE loan_id = #{loanId}")
    int updatestatus(int loan_id);

    @Update("UPDATE loan SET status = 'settled' WHERE loan_id = #{loanId}")
    int updatestatusover(int loan_id);
    @Select("SELECT * FROM loan WHERE borrow_id = #{borrowId}")
    List<Loan> getLoansByUserId(@Param("borrowId") Long borrow_id);
}
