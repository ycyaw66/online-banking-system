package com.zjuse.bankingsystem.mapper.foreignCurrency;

import org.apache.ibatis.annotations.*;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.foreignCurrency.DataOperator;
import com.zjuse.bankingsystem.entity.foreignCurrency.HistoryOperationRecord;

import java.util.List;

@Mapper
public interface FcAdminMapper {


    @Select("select * from data_operator")
    List<DataOperator> queryOperator();

    @Select("select record_id as id,data_operator_id, fc_id, operation, old_rate, new_rate, dest_date, operation_time from history_operation_record")
    List<HistoryOperationRecord> queryOperationRecord();

    @Insert("insert into data_operator(data_operator_id, username, password, email,"+
            "phone_number, add_permission, update_permission, delete_permission)"+
            " values(#{data_operator_id}, #{username}, #{password}, #{email}, #{phone_number}, #{add_permission}, #{update_permission}, #{delete_permission})")
    int insertOperator(DataOperator dataOperator);

    @Delete("delete from data_operator where data_operator_id = #{id}")
    int deleteOperator(String id);

    @Update("update data_operator set add_permission=#{addPermission},update_permission = #{updatePermission}, delete_permission = #{deletePermission} where data_operator_id = #{id}")
    int updateOperatorPermission(String id,int addPermission,int updatePermission,int deletePermission);
}
