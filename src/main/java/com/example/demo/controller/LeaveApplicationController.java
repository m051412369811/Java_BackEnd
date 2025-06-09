package com.example.demo.controller;

import java.time.LocalDate;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid; // 用於Controller參數

import com.example.demo.dto.BaseApiResponse;
import com.example.demo.dto.LeaveApplicationRequestDTO;
import com.example.demo.dto.LeaveApplicationResponseDTO;
import com.example.demo.dto.LeaveApplicationSummaryDTO;
import com.example.demo.dto.LeaveTypeDTO;
import com.example.demo.service.LeaveApplicationService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api/leaveapplications")
public class LeaveApplicationController {
    @Autowired
    private LeaveApplicationService service;

    @GetMapping("/summary")
    public List<LeaveApplicationSummaryDTO> getLeaveApplicationSummaries() {
        return service.getAllLeaveApplicationSummaries();
    }

    @PostMapping("/applyingleaveapplication")
    public BaseApiResponse<LeaveApplicationResponseDTO> createLeaveApplication(
            @Valid @RequestBody LeaveApplicationRequestDTO dto, HttpSession session) {
        try {
            Integer employeeId = (Integer) session.getAttribute("EMPLOYEE_ID");
            if (employeeId == null) {
                return new BaseApiResponse<>("尚未登入，無法申請請假");
            }

            dto.setEmployeeId(employeeId); // 用 session 的值強制設定
            dto.setApplyDate(LocalDate.now());
            ;

            LeaveApplicationResponseDTO result = service.createLeaveApplication(dto);
            return new BaseApiResponse<>(result);
        } catch (Exception ex) {
            return new BaseApiResponse<>(ex.getMessage());
        }

    }

    @GetMapping("/getallleavetype")
    public BaseApiResponse<List<LeaveTypeDTO>> getAllLeavType() {

        try {
            List<LeaveTypeDTO> result = service.getAllLeaveType();
            return new BaseApiResponse<>(result);
        } catch (Exception ex) {
            return new BaseApiResponse<>(ex.getMessage());
        }
    }

}
