package com.zjuse.bankingsystem.mapper.loan;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IdgetMapper  {

    @Select("SELECT officer_id FROM officer WHERE permissions = #{permission} ORDER BY RAND() LIMIT 1")

    int idrandomget(@Param("permission") String permission);

}
