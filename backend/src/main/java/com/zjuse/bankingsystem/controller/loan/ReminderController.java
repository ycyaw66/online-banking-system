package com.zjuse.bankingsystem.controller.loan;

import com.zjuse.bankingsystem.entity.loan.Loan;
import com.zjuse.bankingsystem.entity.loan.Reminder;
import com.zjuse.bankingsystem.security.service.CurrentUserService;
import com.zjuse.bankingsystem.service.loan.ReminderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
public class ReminderController {

    @Autowired
    private ReminderService reminderService;

    @Autowired
    private CurrentUserService currentUserService;

    @PostMapping("/save-reminder")
    public ResponseEntity<String> saveReminder(@RequestBody Reminder reminder) {
        int user_id = ((Number)currentUserService.getCurrentUserId().payload).intValue();;
        reminder.setUserId(user_id);
        reminderService.saveOrUpdateReminder(reminder);
        return ResponseEntity.ok("Reminder saved successfully");
    }

    @GetMapping("/timeget-reminder")
    public ResponseEntity<Integer> getReminder() {
        int userId =  ((Number)currentUserService.getCurrentUserId().payload).intValue();;
        Integer time = reminderService.gettime(userId);
        if (time != null) {
            return ResponseEntity.ok(time);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/loanget-reminder")
    public List<Loan> getLoanHistory(@RequestParam int time) {
        int userId = ((Number)currentUserService.getCurrentUserId().payload).intValue();;
        return reminderService.getLoansToRemind(userId, time);
    }
}