package com.example.demo.dto;

import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveApplicationRequestDTO {

    // private Integer employeeId;
    // @NotNull
    // private Integer leaveTypeId;

    // private String description;
    // @NotNull
    // @Min(1)
    // private Integer leaveDay;
    // @NotNull
    // private LocalDate leaveStart;
    // @NotNull
    // private LocalDate leaveEnd;

    // private LocalDate applyDate;
    // @NotNull
    // private Integer statusId;

    @NotNull(message = "請假類型不得為空")
    private Integer leaveTypeId;

    @NotNull(message = "開始日期不得為空")
    private LocalDate startDate;

    @NotNull(message = "結束日期不得為空")
    private LocalDate endDate;

    private String description;

}
