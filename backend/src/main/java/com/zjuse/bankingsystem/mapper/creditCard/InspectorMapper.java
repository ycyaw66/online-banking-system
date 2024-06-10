package com.zjuse.bankingsystem.mapper.creditCard;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.creditCard.CreditCardInspector;

@Mapper
public interface InspectorMapper extends BaseMapper<CreditCardInspector> {
    @Select("select * from credit_card_inspector where name = #{name} and password = #{password}")
    public CreditCardInspector loginInspector(String name, String password);

    @Select("select * from credit_card_inspector")
    public List<CreditCardInspector> queryInspectors();

    @Update("update credit_card_inspector set password = #{password} where id = #{id}")
    public void modifyInspectorPassword(Integer id, String password);

    @Update("update credit_card_inspector set permission = #{permission} where id = #{id}")
    public void modifyInspectorLevel(Integer id, Integer permission);

    @Update("delete from credit_card_inspector where id = #{id}")
    public void deleteInspector(Integer id);

    @Update("insert into credit_card_inspector (name, password, permission) VALUES ( #{name}, #{password}, #{permission})")
    public void addNewInspector(String name, String password, Integer permission);
}
