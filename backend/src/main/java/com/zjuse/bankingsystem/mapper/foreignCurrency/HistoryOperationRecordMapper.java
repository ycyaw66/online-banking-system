package com.zjuse.bankingsystem.mapper.foreignCurrency;

import java.util.List;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import com.zjuse.bankingsystem.entity.foreignCurrency.HistoryOperationRecord;

@Mapper
public interface HistoryOperationRecordMapper {

    @Select("select * from history_operation_record where data_operator_id = #{data_operator_id}")
    List<HistoryOperationRecord> selectHistoryOperationRecord(String data_operator_id);

    @Insert("insert into history_operation_record(old_rate, new_rate, dest_date, operation_time, record_id, data_operator_id, fc_id, operation) " +
            "values(#{old_rate}, #{new_rate}, #{dest_date}, #{operation_time}, #{record_id}, #{data_operator_id}, #{fc_id}, #{operation})")
    int addHistoryOperationRecord(HistoryOperationRecord historyOperationRecord);

}
