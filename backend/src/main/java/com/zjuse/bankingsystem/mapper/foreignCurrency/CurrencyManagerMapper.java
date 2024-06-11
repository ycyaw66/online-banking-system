package com.zjuse.bankingsystem.mapper.foreignCurrency;

import java.time.LocalDateTime;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zjuse.bankingsystem.entity.foreignCurrency.ForeignCurrency;
import com.zjuse.bankingsystem.entity.foreignCurrency.ForeignCurrencyRate;

import org.apache.ibatis.annotations.Insert;

@Mapper
public interface CurrencyManagerMapper {

    @Select("select * from foreign_currency where fc_name like CONCAT('%', #{fc_name}, '%')")
    public List<ForeignCurrency> selectForeignCurrency(String fc_name);

    @Select("select * from foreign_currency where fc_name = #{fc_name}")
    public ForeignCurrency selectForeignCurrencyExactly(String fc_name);

    @Select("select * from foreign_currency_rate where fc_id = #{fc_id}")
    public List<ForeignCurrencyRate> selectForeignCurrencyRate(String fc_id);

    @Select("select * from foreign_currency_rate where fc_id = #{fc_id} and fc_date = #{fc_date}")
    public ForeignCurrencyRate selectForeignCurrencyRateExactly(String fc_id, LocalDateTime fc_date);
    
    @Select("select * from foreign_currency")
    public List<ForeignCurrency> selectAllForeignCurrency();

    @Insert("insert into foreign_currency_rate(fc_id, fc_rate, fc_date) values(#{fc_id}, #{fc_rate}, Now())")
    public int addCurrencyRate(String fc_id, double fc_rate);

    @Update("update foreign_currency_rate set fc_rate = #{fc_rate} where fc_id = #{fc_id} and fc_date = #{fc_date}")
    public int updateCurrencyRate(String fc_id, double fc_rate, LocalDateTime fc_date);
}
