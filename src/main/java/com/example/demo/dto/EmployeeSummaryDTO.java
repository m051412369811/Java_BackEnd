package com.example.demo.dto;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class EmployeeSummaryDTO {
    private Integer id;
    private String fullName;
    private String departmentName;
    private String titleName;
    private LocalDate hireDate;
}
