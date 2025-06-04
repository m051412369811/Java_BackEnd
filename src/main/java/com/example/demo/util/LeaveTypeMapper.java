package com.example.demo.util;

import org.springframework.stereotype.Component;
import com.example.demo.dto.LeaveTypeDTO;
import com.example.demo.entity.LeaveType;

@Component
public class LeaveTypeMapper {
    public LeaveTypeDTO toLeaveTypeDTO(LeaveType entity) {
        return new LeaveTypeDTO(entity.getId(), entity.getType());
    }
}
