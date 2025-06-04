package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LeaveTypeDTO {
    private Integer value; // 對應LeaveTypeID
    private String label; // 對應LeaveType
}
