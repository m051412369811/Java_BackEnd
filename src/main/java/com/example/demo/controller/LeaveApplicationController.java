package com.example.demo.controller;

import java.time.LocalDate;
import java.util.Collections;
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
    public List<LeaveApplicationSummaryDTO> getLeaveApplicationSummaries(HttpSession session) {
        // 1. 從 session 中獲取登入時存入的員工 ID
        Object empIdFromSession = session.getAttribute("EMPLOYEE_ID");
        // 2. 檢查 session 中是否存在員工 ID (使用者是否登入)
        if (empIdFromSession != null) {
            // 將 Object 型別轉換為 Integer
            Integer employeeId = (Integer) empIdFromSession;
            // 3. 呼叫 service 方法，並傳入從 session 拿到的 ID
            return service.getLeaveApplicationSummariesByEmployeeId(employeeId);
        } else {
            // 如果使用者未登入，回傳一個空的列表
            // 這樣可以避免前端出錯，同時也保護了資料
            return Collections.emptyList();
        }
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
