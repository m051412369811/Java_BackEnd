package com.example.demo.dto;

import java.sql.Date;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LeaveApplicationRequestDTO {

    @NotNull
    private Integer employeeId;
    @NotNull
    private Date applyDate;
    @NotNull
    private Integer leaveTypeId;
    @NotNull
    @Min(1)
    private Integer leaveDay;
    @NotNull
    private Date leaveStart;
    @NotNull
    private Date leaveEnd;
    @NotNull
    private Integer statusId;

}
