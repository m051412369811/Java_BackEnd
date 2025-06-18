package com.example.demo.dto;

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

    private String description;
    @NotNull
    @Min(1)
    private Integer leaveDay;
    @NotNull
    private LocalDate leaveStart;
    @NotNull
    private LocalDate leaveEnd;

    private LocalDate applyDate;
    @NotNull
    private Integer statusId;

}
