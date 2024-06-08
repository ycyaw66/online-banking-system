package com.zjuse.bankingsystem.mapper.creditCard;

import java.math.BigDecimal;
import java.util.List;

import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.creditCard.CreditCardApplication;

public interface CreditCardApplicationMapper extends BaseMapper<CreditCardApplication>{
    @Update("insert into credit_card_application (id_number, credit_card_id, amount, type, status, password) values (#{id_number}, null, #{card_limit}, 1, 1, #{password})")
    public void addNewCreditCardRequest(String id_number, BigDecimal card_limit, String password);

    @Update("insert into credit_card_application (id_number, credit_card_id, amount ,type, status, password) values ( #{id_number}, #{card_id}, #{limit}, 2, 1, null)")
    public void addModifyLimitRequest(String id_number, Long card_id, BigDecimal limit);

    @Select("select * from credit_card_application where id_number = #{idNumber}")
    public List<CreditCardApplication> queryAllRequestsByCustomer(String idNumber);

    @Select("select * from credit_card_application where status = 1")
    public List<CreditCardApplication> queryAllRequestByInspector();

    @Select("select * from credit_card_application where status = 1 and type = 1")
    public List<CreditCardApplication> queryPartRequestByInspector();

    @Select("select * from credit_card_application where id = #{id}")
    public CreditCardApplication selectSingleRequest(Long id);

    @Update("update credit_card_application set status = 2 where id = #{id}")
    public void acceptRequest(Long id);

    @Update("update credit_card_application set status = 3 where id = #{id}")
    public void rejectRequest(Long id);
}
