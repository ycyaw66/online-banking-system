package com.zjuse.bankingsystem.mapper.loan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.loan.Form;
import org.apache.ibatis.annotations.Mapper;

@Mapper
                                        // here is bank_card !!! not form
public interface CardgetMapper extends BaseMapper<Form>{

    //���Ǽ����û������п�����bank_card��
    //@Select("SELECT * FROM bank_card WHERE user_id = #{userId}")
    //List<BankCard> getUserBankCards(@Param("userId") int userId);

}
