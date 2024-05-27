package com.zjuse.bankingsystem.mapper;

import com.zjuse.bankingsystem.entity.creditCard.*;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.apache.ibatis.annotations.UpdateProvider;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

@Mapper
public interface CreditCardMapper {

    @Select("select * from credit_card where id_number = #{id_number}")
    public List<CreditCard> queryCards(String id_number);

    @Update("insert into credit_card_application (id, id_number, credit_card_id, amount, type, status, password) values (#{cardId}, #{id_number}, null, #{card_limit}, 1, 1, #{password})")
    public void addNewCreditCardRequest(Long cardId, String id_number, BigDecimal card_limit, String password);

    @Update("update credit_card set password = #{password} where id = #{card_id}")
    public void modifyCreditCardPassword(Long card_id, String password);

    @Update("insert into credit_card_application (id_number, credit_card_id, amount ,type, status, password) values ( #{id_number}, #{card_id}, #{limit}, 2, 1, null)")
    public void addModifyLimitRequest(String id_number, Long card_id, BigDecimal limit);

    @Update("update credit_card set loan = loan - #{amount} where id = #{card_id}")
    public void returnMoney(Long card_id, BigDecimal amount);

    @Select("select * from credit_card where id = #{card_id}")
    public CreditCard findCreditCard(Long card_id);

    @Update("update credit_card set is_lost = 1 where id = #{card_id}")
    public void setCreditCardLost(Long card_id);

    @Update("insert into credit_card (id_number, password, card_limit, loan, is_lost) VALUES ( #{idNumber}, #{password}, #{cardLimit}, #{loan}, 0)")
    public void insertCreditCard(CreditCard creditCard);

    @Update("delete from credit_card where id = #{card_id}")
    public void deleteCreditCard(Long card_id);

    @Select("select * from credit_card_admin where name = #{name} and password = #{password}")
    public CreditCardAdmin loginAdmin(String name, String password);

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

    @Select("select * from credit_card_inspector where name = #{name} and password = #{password}")
    public CreditCardInspector loginInspector(String name, String password);

    @Select("select * from credit_card_application where status = 1")
    public List<CreditCardApplication> queryAllRequestByInspector();

    @Select("select * from credit_card_application where status = 1 and type = 1")
    public List<CreditCardApplication> queryPartRequestByInspector();

    @Select("select * from credit_card_application where id = #{id}")
    public CreditCardApplication selectSingleRequest(Integer id);

    @Update("update credit_card set card_limit = #{card_limit} where id = #{id}")
    public void updateCardLimit(BigDecimal card_limit, Long id);

    @Update("update credit_card_application set status = 2 where id = #{id}")
    public void acceptRequest(Integer id);

    @Update("update credit_card_application set status = 3 where id = #{id}")
    public void rejectRequest(Integer id);

    @Select("select * from credit_card_application where id_number = #{idNumber}")
    public List<CreditCardApplication> queryAllRequestsByCustomer(String idNumber);

    @Select("select * from credit_card where id = #{card_id} and password = #{password} and is_lost = 0")
    public CreditCard findMatchCard(Long card_id, String password);

    @Update("insert into credit_card_bill (credit_card_id, amount, bill_date) VALUES ( #{id_number}, #{credit_card_id}, #{amount}, #{bill_date})")
    public void addPayment(Long credit_card_id, BigDecimal amount, Date bill_date);

    @Update("update credit_card set loan = loan + #{account} where id = #{card_id}")
    public void updateLoan(Long card_id, BigDecimal account);

    @Select("select * from credit_card_bill where credit_card_id = #{cardId} and bill_date >= #{start_date} and bill_date <= #{end_date}")
    public List<CreditCardBill> queryBills(Date start_date, Date end_date, Long cardId);
}
