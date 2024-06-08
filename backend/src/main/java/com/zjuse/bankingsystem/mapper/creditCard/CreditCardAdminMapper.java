package com.zjuse.bankingsystem.mapper.creditCard;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.creditCard.CreditCardAdmin;

@Mapper
public interface CreditCardAdminMapper extends BaseMapper<CreditCardAdmin> {
    @Select("select * from credit_card_admin where name = #{name} and password = #{password}")
    public CreditCardAdmin loginAdmin(String name, String password);

}
