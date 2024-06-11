package com.zjuse.bankingsystem.mapper.loan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.loan.Form;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FormMapper extends BaseMapper<Form> {
    @Insert("INSERT INTO form (user_name, id_number, gender, emotion, income, address, phone_number, email, education, purpose, statement) " +
            "VALUES (#{user_name}, #{id_number}, #{gender}, #{emotion}, #{income}, #{address}, #{phone_number}, #{email}, #{education}, #{purpose}, #{statement})")
    @Options(useGeneratedKeys = true, keyProperty = "form_id")
    int insert(Form form);
}
