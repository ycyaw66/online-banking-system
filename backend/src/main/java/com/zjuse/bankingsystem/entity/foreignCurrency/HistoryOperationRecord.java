package com.zjuse.bankingsystem.entity.foreignCurrency;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@AllArgsConstructor
@Getter
@Setter
public class HistoryOperationRecord {
    private String record_id;
    private String data_operator_id;
    private String fc_id;
    private String operation;
    private double old_rate;
    private double new_rate;
    private LocalDateTime dest_date;
    private LocalDateTime operation_time;
}
