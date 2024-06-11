package com.zjuse.bankingsystem.mapper.loan;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.loan.Officer;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface OfficerMapper extends BaseMapper<Officer> {
    @Select("SELECT * FROM officer WHERE username = #{username}")
    Officer findByUsername(String username);
}
