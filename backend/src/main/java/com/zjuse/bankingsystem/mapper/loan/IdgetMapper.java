package com.zjuse.bankingsystem.mapper.loan;


import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface IdgetMapper  {

    @Select("SELECT id FROM officer WHERE permission = #{permission} ORDER BY RAND() LIMIT 1")

    int idrandomget(@Param("permission") String permission);

}
