package com.example.demo.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.dto.LeaveApplicationRequestDTO;
import com.example.demo.dto.LeaveApplicationResponseDTO;
import com.example.demo.dto.LeaveApplicationSummaryDTO;
import com.example.demo.entity.Employee;
import com.example.demo.entity.LeaveApplication;
import com.example.demo.entity.LeaveType;
import com.example.demo.entity.StatusType;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.LeaveApplicationRepository;
import com.example.demo.repository.LeaveTypeRepository;
import com.example.demo.repository.StatusTypeRepository;
import com.example.demo.util.LeaveApplicationMapper;

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
    private LeaveApplicationMapper mapper;

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
        LeaveApplication entity = mapper.toEntity(dto, employee, leaveType, status);
        LeaveApplication savedLeaveApplication = repository.save(entity);
        // 3.
        return mapper.toResponseDTO(savedLeaveApplication);
    }

}
