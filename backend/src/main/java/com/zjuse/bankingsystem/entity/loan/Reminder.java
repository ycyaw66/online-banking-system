package com.zjuse.bankingsystem.entity.loan;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Reminder {
    @JsonProperty("reminder_id")
    private int reminderId;

    @JsonProperty("user_id")
    private int userId;

    @JsonProperty("time")
    private int time;

    // Getters and setters


    public int getReminderId() {
        return reminderId;
    }

    public void setReminderId(int reminderId) {
        this.reminderId = reminderId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }
}