package com.zjuse.bankingsystem.mapper.creditCard;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.creditCard.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface CreditCardMapper extends BaseMapper<CreditCard>{

    @Select("select * from credit_card where id_number = #{id_number}")
    public List<CreditCard> queryCards(String id_number);
    

    @Update("update credit_card set password = #{password} where id = #{card_id}")
    public void modifyCreditCardPassword(Long card_id, String password);


    @Update("update credit_card set loan = loan - #{amount} where id = #{card_id}")
    public void returnMoney(Long card_id, BigDecimal amount);

    @Select("select * from credit_card where id = #{card_id}")
    public CreditCard findCreditCard(Long card_id);

    @Update("update credit_card set is_lost = 1 where id = #{card_id}")
    public void setCreditCardLost(Long card_id);

    @Update("insert into credit_card (id, id_number, password, card_limit, loan, is_lost) VALUES (#{id}, #{idNumber}, #{password}, #{cardLimit}, #{loan}, 0)")
    public void insertCreditCard(CreditCard creditCard);

    @Update("delete from credit_card where id = #{card_id}")
    public void deleteCreditCard(Long card_id);

    @Select("select * from credit_card where id = #{card_id} and password = #{password} and is_lost = 0")
    public CreditCard findMatchCard(Long card_id, String password);

    @Update("insert into credit_card_bill (credit_card_id, id_number, amount, bill_date) VALUES ( #{credit_card_id}, #{idNumber}, #{amount}, #{bill_date})")
    public void addPayment(Long credit_card_id, String idNumber, BigDecimal amount, Date bill_date);

    @Update("update credit_card set loan = loan + #{account} where id = #{card_id}")
    public void updateLoan(Long card_id, BigDecimal account);

    @Select("select * from credit_card_bill where id_number = #{idNumber} and bill_date >= #{start_date} and bill_date <= #{end_date}")
    public List<CreditCardBill> queryBills(Date start_date, Date end_date, String idNumber);

    @Update("update credit_card set card_limit = #{card_limit} where id = #{id}")
    public void updateCardLimit(BigDecimal card_limit, Long id);
}
