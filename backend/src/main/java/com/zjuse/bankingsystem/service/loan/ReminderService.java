package com.zjuse.bankingsystem.service.loan;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.zjuse.bankingsystem.entity.loan.Loan;
import com.zjuse.bankingsystem.entity.loan.Reminder;
import com.zjuse.bankingsystem.mapper.loan.LoanQueryMapper;
import com.zjuse.bankingsystem.mapper.loan.ReminderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ReminderService {

    @Autowired
    private ReminderMapper reminderMapper;

    @Autowired
    private LoanQueryMapper loanQueryMapper;

    public void saveOrUpdateReminder(Reminder reminder) {
        Integer existingReminderTime = reminderMapper.getReminderTime(reminder.getUserId());
        if (existingReminderTime == null) {
            // 插入新的提醒设置
            reminderMapper.insertReminder(reminder.getUserId(), reminder.getTime());
        } else {
            // 更新现有的提醒设置
            reminderMapper.updateReminder(reminder.getUserId(), reminder.getTime());
        }
    }

    public List<Loan> getLoansToRemind(int userId, int time) {
        // 获取当前日期
        // 查询符合条件的贷款记录
        return loanQueryMapper.selectList(new QueryWrapper<Loan>()
                .eq("borrow_id", userId)
                .eq("status", "repayment") // 未还款状态
                .apply("DATE_ADD(date_approved, INTERVAL term DAY) - INTERVAL {0} DAY <= NOW()", time));
    }


    public Integer gettime(int user_id){ return reminderMapper.getReminderTime(user_id);}
}