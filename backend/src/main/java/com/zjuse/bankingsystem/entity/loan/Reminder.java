package com.zjuse.bankingsystem.entity.loan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

public class Reminder {
    @TableId(value = "reminder_id", type = IdType.AUTO)
    private int reminder_id;

    @TableField("user_id")
    private int user_id;

    @TableField("time")
    private int time;

    // Getters and setters


    public int getReminder_id() {
        return reminder_id;
    }

    public void setReminder_id(int reminder_id) {
        this.reminder_id = reminder_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}