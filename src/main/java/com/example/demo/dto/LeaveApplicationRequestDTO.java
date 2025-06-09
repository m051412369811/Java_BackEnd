package com.example.demo.dto;

import java.sql.Date;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveApplicationRequestDTO {

    private Integer employeeId;
    @NotNull
    private Integer leaveTypeId;
    @NotNull
    @Min(1)
    private Integer leaveDay;
    @NotNull
    private Date leaveStart;
    @NotNull
    private Date leaveEnd;

    private LocalDate applyDate;
    @NotNull
    private Integer statusId;

}
