package com.zjuse.bankingsystem.mapper.foreignCurrency;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zjuse.bankingsystem.entity.foreignCurrency.ForeignCurrencyAccount;

import java.util.List;

@Mapper
public interface ForeignCurrencyAccountMapper {
    @Select("SELECT * FROM foreign_currency_account WHERE credit_card_id = #{credit_card_id}")
    List<ForeignCurrencyAccount> findAccountsByCreditCardId(@Param("credit_card_id") String credit_card_id);

    @Insert("INSERT INTO foreign_currency_account (credit_card_id, fc_id, amount) " +
            "VALUES (#{credit_card_id}, #{fc_id}, #{amount})")
    void insertForeignCurrencyAccount(ForeignCurrencyAccount foreignCurrencyAccount);

    @Select("SELECT * FROM foreign_currency_account WHERE credit_card_id = #{credit_card_id} AND fc_id = #{fc_id}")
    ForeignCurrencyAccount findAccountsByCreditCardIdAndFcId(@Param("credit_card_id") String credit_card_id, @Param("fc_id") String fc_id);

    @Update("UPDATE foreign_currency_account SET amount = #{amount} WHERE credit_card_id = #{credit_card_id} AND fc_id = #{fc_id}")
    void updateForeignCurrencyAccount(ForeignCurrencyAccount foreignCurrencyAccount);
}
