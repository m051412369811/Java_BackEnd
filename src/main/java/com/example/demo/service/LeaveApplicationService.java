package com.example.demo.service;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.util.*;

@Service
public class LeaveApplicationService {
    @Autowired
    private LeaveApplicationRepository repository;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveTypeRepository leaveTypeRepository;
    @Autowired
    private StatusTypeRepository statusTypeRepository;
    @Autowired
    private LeaveApplicationMapper leaveApplicationMapper;
    @Autowired
    private LeaveTypeMapper leaveTypeMapper;

    public List<LeaveApplicationSummaryDTO> getAllLeaveApplicationSummaries() {
        System.out.println("JPQL findAllSummary() 被呼叫");
        return repository.findAllSummary();
    }

    public LeaveApplicationResponseDTO createLeaveApplication(LeaveApplicationRequestDTO dto) {
        // 1. 查找關聯物件
        Employee employee = employeeRepository.findById(dto.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("員工不存在"));
        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new RuntimeException("假別不存在"));
        StatusType status = statusTypeRepository.findById(dto.getStatusId())
                .orElseThrow(() -> new RuntimeException("狀態不存在"));

        // 2. Mapper轉換物件型態
        LeaveApplication entity = leaveApplicationMapper.toEntity(dto, employee, leaveType, status);
        LeaveApplication savedLeaveApplication = repository.save(entity);
        // 3.
        return leaveApplicationMapper.toResponseDTO(savedLeaveApplication);
    }

    public List<LeaveTypeDTO> getAllLeaveType() {
        return leaveTypeRepository.findAll()
                .stream()
                .map(leaveTypeMapper::toLeaveTypeDTO)
                .collect(Collectors.toList());

    }

}
