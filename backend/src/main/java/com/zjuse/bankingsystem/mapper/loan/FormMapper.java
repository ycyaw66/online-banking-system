package com.zjuse.bankingsystem.mapper.loan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.loan.Form;
import com.zjuse.bankingsystem.entity.loan.Officer;
import org.apache.ibatis.annotations.*;

@Mapper
public interface FormMapper extends BaseMapper<Form> {
    @Insert("INSERT INTO form ( user_name, id_number, gender, emotion, income, address, phone_number, email, education, purpose, statement) " +
            "VALUES (#{userName}, #{idNumber}, #{gender}, #{emotion}, #{income}, #{address}, #{phoneNumber}, #{email}, #{education}, #{purpose}, #{statement})")
    @Options(useGeneratedKeys = true, keyProperty = "formId")
    int insert(Form form);

    @Select("SELECT * FROM form WHERE form_id = #{ss}")
    Form findByID(int ss);
}
