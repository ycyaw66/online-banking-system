package com.zjuse.bankingsystem.mapper;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.creditCard.CreditCardApplication;
import com.zjuse.bankingsystem.entity.creditCard.CreditCardInspector;

@Mapper
public interface InspectorMapper extends BaseMapper<CreditCardInspector> {
    @Select("select * from credit_card_inspector where name = #{name} and password = #{password}")
    public CreditCardInspector loginInspector(String name, String password);

    @Select("select * from credit_card_application where status = 1")
    public List<CreditCardApplication> queryAllRequestByInspector();

    @Select("select * from credit_card_application where status = 1 and type = 1")
    public List<CreditCardApplication> queryPartRequestByInspector();

    @Select("select * from credit_card_application where id = #{id}")
    public CreditCardApplication selectSingleRequest(Long id);

    @Update("update credit_card set card_limit = #{card_limit} where id = #{id}")
    public void updateCardLimit(BigDecimal card_limit, Long id);

    @Update("update credit_card_application set status = 2 where id = #{id}")
    public void acceptRequest(Long id);

    @Update("update credit_card_application set status = 3 where id = #{id}")
    public void rejectRequest(Long id);
}
