package com.example.demo.dto;

import java.sql.Date;
import java.time.LocalDateTime;

public record LeaveApplicationResponseDTO(
        Integer id,
        String employeeName,
        String leaveTypeName,
        Date applyDate,
        Integer leaveDay,
        Date leaveStart,
        Date leaveEnd,
        String statusType,
        LocalDateTime createTime) {
}
