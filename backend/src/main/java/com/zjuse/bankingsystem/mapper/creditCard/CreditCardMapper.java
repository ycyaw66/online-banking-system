package com.zjuse.bankingsystem.mapper.creditCard;

import com.zjuse.bankingsystem.entity.creditCard.CreditCard;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.math.BigInteger;
import java.util.List;

@Mapper
public interface CreditCardMapper {

    @Select("select * from credit_card where id_number = #{id_number}")
    public List<CreditCard> queryCards(String id_number);

    @Update("insert into credit_card_application (id_number, credit_card_id, amount, type, status, password) values (#{id_number}, null, #{card_limit}, 1, 1, #{password})")
    public void addNewCreditCardRequest(String id_number, BigInteger card_limit, String password);

    @Update("update credit_card set password = #{password} where id = #{card_id}")
    public void modifyCreditCardPassword(BigInteger card_id, String password);

    @Update("insert into credit_card_application (id_number, credit_card_id, amount ,type, status, password) values ( #{id_number}, #{card_id}, #{limit}, 2, 1, null)")
    public void addModifyLimitRequest(String id_number, BigInteger card_id, BigInteger limit);

    @Update("update credit_card set loan = loan - #{amount} where id = #{card_id}")
    public void returnMoney(BigInteger card_id, BigInteger amount);

    @Select("select * from credit_card where id = #{card_id}")
    public CreditCard findCreditCard(BigInteger card_id);

    @Update("update credit_card set is_lost = 1 where id = #{card_id}")
    public void setCreditCardLost(BigInteger card_id);

    @Update("insert into credit_card (id_number, password, card_limit, loan, is_lost) VALUES ( #{idNumber}, #{password}, #{cardLimit}, #{loan}, 0)")
    public void insertCreditCard(CreditCard creditCard);

    @Update("delete from credit_card where id = #{card_id}")
    public void deleteCreditCard(BigInteger card_id);

}
