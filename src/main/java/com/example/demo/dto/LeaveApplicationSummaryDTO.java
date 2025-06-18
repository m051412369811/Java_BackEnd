package com.example.demo.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveApplicationSummaryDTO {
    private Integer id;
    private String employeeName;
    private String leaveType;
    private Integer leaveDay;
    private String statusType;
    private LocalDate leaveStart;
    private LocalDate leaveEnd;
    private LocalDate applyDate;
    private String description;

    // Getters/Setters...
}
