package com.zjuse.bankingsystem.mapper.loan;


import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.zjuse.bankingsystem.entity.loan.Reminder;
import org.apache.ibatis.annotations.*;

@Mapper
public interface ReminderMapper extends BaseMapper<Reminder> {

    @Select("SELECT time FROM reminder WHERE user_id = #{userId}")
    Integer getReminderTime(@Param("userId") int userId);

    @Insert("INSERT INTO reminder (user_id, time) VALUES (#{userId}, #{reminderTime})")
    int insertReminder(@Param("userId") int userId, @Param("reminderTime") int reminderTime);

    @Update("UPDATE reminder SET time = #{reminderTime} WHERE user_id = #{userId}")
    int updateReminder(@Param("userId") int userId, @Param("reminderTime") int reminderTime);
}

