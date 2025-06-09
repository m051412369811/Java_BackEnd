package com.example.demo.dto;

import java.sql.Date;
import java.time.LocalDate;

import lombok.Data;

@Data
public class LeaveApplicationSummaryDTO {
    private Integer id;
    private String employeeName;
    private String leaveType;
    private Integer leaveDay;
    private String statusType;
    private Date leaveStart;
    private Date leaveEnd;
    private LocalDate applyDate;

    public LeaveApplicationSummaryDTO(
            Integer id, String employeeName, String leaveType, Integer leaveDay, String statusType,
            Date leaveStart, Date leaveEnd, LocalDate applyDate) {
        this.id = id;
        this.employeeName = employeeName;
        this.leaveType = leaveType;
        this.leaveDay = leaveDay;
        this.statusType = statusType;
        this.leaveStart = leaveStart;
        this.leaveEnd = leaveEnd;
        this.applyDate = applyDate;
    }
    // Getters/Setters...
}
