package com.example.demo.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LeaveApplicationResponseDTO(
                Integer id,
                String employeeName,
                String leaveTypeName,
                LocalDate applyDate,
                Integer leaveDay,
                LocalDate leaveStart,
                LocalDate leaveEnd,
                String description,
                String statusType,
                LocalDateTime createTime) {
}
