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

    @Select("SELECT amount FROM loan WHERE loan_id = #{loan_id}")
    double getAmount(@Param("loan_id") int loan_id);

    @Select("SELECT rate FROM loan WHERE loan_id = #{loan_id}")
    double getrate(@Param("loan_id") int loan_id);

    @Update("UPDATE loan SET status = repayment WHERE loan_id = #{loan_id}")
    int updatestatus(int loan_id);

     @Select("SELECT * FROM loan WHERE borrow_id = #{borrow_id}")
    List<Loan> getLoansByUserId(@Param("borrow_id") Long borrow_id);
}
