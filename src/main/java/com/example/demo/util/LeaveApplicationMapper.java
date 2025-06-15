package com.example.demo.util;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.stereotype.Component;

import com.example.demo.dto.LeaveApplicationRequestDTO;
import com.example.demo.dto.LeaveApplicationResponseDTO;
import com.example.demo.entity.Employee;
import com.example.demo.entity.LeaveApplication;
import com.example.demo.entity.LeaveType;
import com.example.demo.entity.StatusType;

@Component
public class LeaveApplicationMapper {
    // DTO → Entity（需要關聯Entity物件）
    public LeaveApplication toEntity(
            LeaveApplicationRequestDTO dto,
            Employee employee,
            LeaveType leaveType,
            StatusType status,
            LocalDate applyTime) {
        LeaveApplication entity = new LeaveApplication();
        entity.setEmployee(employee);
        entity.setLeaveType(leaveType);
        entity.setLeaveDay(dto.getLeaveDay());
        entity.setLeaveStart(dto.getLeaveStart());
        entity.setLeaveEnd(dto.getLeaveEnd());
        entity.setStatus(status);
        entity.setApplyDate(applyTime);
        entity.setDescription(dto.getDescription());
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        return entity;
    }

    // Entity → Response DTO
    public LeaveApplicationResponseDTO toResponseDTO(LeaveApplication entity) {
        return new LeaveApplicationResponseDTO(
                entity.getId(),
                entity.getEmployee().getFirstName() + " " + entity.getEmployee().getLastName(),
                entity.getLeaveType().getType(),
                entity.getApplyDate(),
                entity.getLeaveDay(),
                entity.getLeaveStart(),
                entity.getLeaveEnd(),
                entity.getDescription(),
                entity.getStatus().getType(),
                entity.getCreateTime());
    }
}
