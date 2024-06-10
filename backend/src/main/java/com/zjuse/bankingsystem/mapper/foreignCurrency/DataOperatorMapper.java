package com.zjuse.bankingsystem.mapper.foreignCurrency;

import java.util.List;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.zjuse.bankingsystem.entity.foreignCurrency.DataOperator;

@Mapper
public interface DataOperatorMapper {

    @Select("select * from data_operator")
    List<DataOperator> selectAllDataOperator();

    @Select("select * from data_operator where username = #{username}")
    DataOperator selectDataOperatorByUsername(String username);

    @Select("select * from data_operator where data_operator_id = #{data_operator_id}")
    DataOperator selectDataOperatorById(String data_operator_id);

    @Insert("insert into data_operator(data_operator_id, username, password, email,"+
     "phone_number, add_permission, update_permission, delete_permission)"+
     " values(#{data_operator_id}, #{username}, #{password}, #{email}, #{phone_number}, #{add_permission}, #{update_permission}, #{delete_permission})")
    int insertDataOperator(DataOperator dataOperator);

    @Insert("insert into data_operator(data_operator_id, username, password) value(#{data_operator_id}, #{username}, #{password})")
    int insertDataOperatorBasic(String data_operator_id, String username, String password);

    @Update("update data_operator set email = #{email}, phone_number = #{phone_number}, add_permission = #{add_permission}, update_permission = #{update_permission}, delete_permission = #{delete_permission}"
    +" where data_operator_id = #{data_operator_id}")
    int updateDataOperatorDetail(DataOperator dataOperator);

    @Update("update data_operator set password = #{password} where data_operator_id = #{data_operator_id}")
    int updateDataOperatorPassword(String data_operator_id, String password);

    @Delete("delete from data_operator where data_operator_id = #{data_operator_id}")
    int deleteDataOperator(String data_operator_id);
}
