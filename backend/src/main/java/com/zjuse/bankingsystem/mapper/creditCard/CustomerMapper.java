package com.zjuse.bankingsystem.mapper.creditCard;

import com.zjuse.bankingsystem.entity.creditCard.CreditCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CustomerMapper {

    @Select("select * from credit_card where id_number = #{id_number}")
    public List<CreditCard> queryCards(String id_number);

}
