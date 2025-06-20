package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import com.example.demo.dto.*;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.util.*;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LeaveApplicationService {

    private final LeaveApplicationRepository leaveApplicationRepository;
    private final LeaveApprovalRepository leaveApprovalRepository;
    private final EmployeeRepository employeeRepository;
    private final LeaveTypeRepository leaveTypeRepository;
    private final StatusTypeRepository statusTypeRepository;
    private final LeaveTypeMapper leaveTypeMapper;

    // 直接定義狀態對應的數字id
    private static final int STATUS_PENDING_ID = 1;
    private static final int STATUS_APPROVED_ID = 2;

    public List<LeaveApplicationSummaryDTO> getLeaveApplicationSummariesByEmployeeId(Integer empId) {
        System.out.println("JPQL findAllSummary() 被呼叫");
        return leaveApplicationRepository.findSummaryByEmployeeId(empId);
    }

    public List<LeaveTypeDTO> getAllLeaveType() {
        return leaveTypeRepository.findAll()
                .stream()
                .map(leaveTypeMapper::toLeaveTypeDTO)
                .collect(Collectors.toList());

    }

    @Transactional
    public LeaveApplication createLeaveApplication(LeaveApplicationRequestDTO dto, Integer applicantId) {

        // 步驟 1: 驗證並獲取所有需要的 Entity 物件
        Employee applicant = employeeRepository.findById(applicantId)
                .orElseThrow(() -> new IllegalArgumentException("無效的員工ID: " + applicantId));

        LeaveType leaveType = leaveTypeRepository.findById(dto.getLeaveTypeId())
                .orElseThrow(() -> new IllegalArgumentException("無效的假別ID: " + dto.getLeaveTypeId()));

        // 步驟 2: 執行後端權威計算
        // 這裡先用簡化邏輯計算天數
        int leaveDays = (int) java.time.temporal.ChronoUnit.DAYS.between(dto.getStartDate(), dto.getEndDate()) + 1;
        if (leaveDays <= 0) {
            throw new IllegalArgumentException("結束日期不能早于開始日期");
        }

        // 步驟 3: 組裝並儲存主假單
        LeaveApplication application = buildLeaveApplicationEntity(dto, applicant, leaveType, leaveDays);
        LeaveApplication savedApplication = leaveApplicationRepository.save(application);

        // 步驟 4: 根據天數，執行審核流程規則引擎
        applyApprovalRules(savedApplication);

        // 再次儲存以更新可能變動的狀態（例如直接批准的情況）
        return leaveApplicationRepository.save(savedApplication);
    }

    // 負責組裝 LeaveApplication Entity。

    private LeaveApplication buildLeaveApplicationEntity(LeaveApplicationRequestDTO dto, Employee applicant,
            LeaveType leaveType, int leaveDays) {
        LeaveApplication entity = new LeaveApplication();
        entity.setEmployee(applicant);
        entity.setLeaveType(leaveType);
        entity.setStartDate(dto.getStartDate());
        entity.setEndDate(dto.getEndDate());
        entity.setDescription(dto.getDescription());
        entity.setApplyDate(LocalDate.now());
        entity.setLeaveDays(leaveDays);

        StatusType pendingStatus = statusTypeRepository.findById(STATUS_PENDING_ID)
                .orElseThrow(() -> new IllegalStateException("資料庫中找不到ID為 " + STATUS_PENDING_ID + " 的 Pending 狀態"));
        entity.setStatus(pendingStatus);

        return entity;
    }

    // 負責執行審核規則。
    private void applyApprovalRules(LeaveApplication application) {
        // 規則一：2天(含)以內，直接批准
        if (application.getLeaveDays() <= 2) {
            StatusType approvedStatus = statusTypeRepository.findById(STATUS_APPROVED_ID)
                    .orElseThrow(
                            () -> new IllegalStateException("資料庫中找不到ID為 " + STATUS_APPROVED_ID + " 的 Approved 狀態"));
            application.setStatus(approvedStatus);
            // 不需要建立審核步驟
            return;
        }

        StatusType pendingStatus = statusTypeRepository.findById(STATUS_PENDING_ID).get();
        Employee manager = application.getEmployee().getManager();
        if (manager == null) {
            throw new IllegalStateException("該員工沒有設定直屬主管，無法送出需要審核的假單");
        }

        // 規則二：3-5天，需要一階段審核
        createApprovalStep(application, manager, 1, pendingStatus);

        // 規則三：超過5天，需要二階段審核
        if (application.getLeaveDays() > 5) {
            Employee seniorManager = manager.getManager();
            if (seniorManager == null) {
                throw new IllegalStateException("該員工的主管沒有更高階主管，無法送出二階審核");
            }
            createApprovalStep(application, seniorManager, 2, pendingStatus);
        }
    }

    private void createApprovalStep(LeaveApplication application, Employee approver, int level, StatusType status) {
        LeaveApproval approvalStep = new LeaveApproval();
        approvalStep.setLeaveApplication(application);
        approvalStep.setApprover(approver);
        approvalStep.setApprovalLevel(level);
        approvalStep.setStatusType(status);
        leaveApprovalRepository.save(approvalStep);
    }

}
