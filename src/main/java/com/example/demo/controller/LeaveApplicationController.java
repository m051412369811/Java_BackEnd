package com.example.demo.controller;

import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid; // 用於Controller參數

import com.example.demo.dto.BaseApiResponse;
import com.example.demo.dto.LeaveApplicationRequestDTO;
import com.example.demo.dto.LeaveApplicationSummaryDTO;
import com.example.demo.dto.LeaveTypeDTO;
import com.example.demo.entity.LeaveApplication;
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
    public BaseApiResponse<List<LeaveApplicationSummaryDTO>> getLeaveApplicationSummaries(HttpSession session) {
        try {
            Object empIdFromSession = session.getAttribute("EMPLOYEE_ID");
            if (empIdFromSession != null) {
                Integer employeeId = (Integer) empIdFromSession;
                List<LeaveApplicationSummaryDTO> list = service.getLeaveApplicationSummariesByEmployeeId(employeeId);
                // 查無資料會自動是空 list
                return new BaseApiResponse<>(list); // 成功
            } else {
                // 未登入，仍回傳空 list 但標註失敗
                return new BaseApiResponse<>(Collections.emptyList(), false, "尚未登入");
            }
        } catch (Exception ex) {
            // 例外情況也回空 list 並帶錯誤訊息
            return new BaseApiResponse<>(Collections.emptyList(), false, ex.getMessage());
        }
    }

    @PostMapping("/applyingleaveapplication")
    public ResponseEntity<BaseApiResponse<Integer>> createLeaveApplication(
            @Valid @RequestBody LeaveApplicationRequestDTO dto,
            HttpSession session) {

        Integer employeeId = (Integer) session.getAttribute("EMPLOYEE_ID");
        if (employeeId == null) {
            // 使用標準 HTTP 401 Unauthorized 狀態碼回傳未登入錯誤
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new BaseApiResponse<>("尚未登入，無法申請請假"));
        }

        try {
            // Controller 只需傳遞最原始的資料，將所有業務邏輯完全交給 Service
            LeaveApplication createdApplication = service.createLeaveApplication(dto, employeeId);
            // 新增成功，回傳標準的 HTTP 201 Created 狀態碼，並在 body 中附上新假單的 ID
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BaseApiResponse<>(createdApplication.getId()));
        } catch (IllegalArgumentException | IllegalStateException e) {
            // 對於可預期的業務邏輯錯誤（如找不到主管、日期錯誤），回傳 400 Bad Request
            return ResponseEntity.badRequest().body(new BaseApiResponse<>(e.getMessage()));
        } catch (Exception e) {
            // 對於非預期的伺服器內部錯誤，回傳 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse<>("系統發生未預期的錯誤，請聯繫管理員"));
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
