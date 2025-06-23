package com.example.demo.service;

import com.example.demo.dto.PendingApprovalDTO;
import com.example.demo.entity.LeaveApproval; // 假設您的審核步驟 Entity 名稱
import com.example.demo.entity.LeaveApplication;
import com.example.demo.entity.StatusType;
import com.example.demo.repository.LeaveApprovalRepository;
import com.example.demo.repository.LeaveApplicationRepository;
import com.example.demo.repository.StatusTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApprovalService {

    private final LeaveApprovalRepository leaveApprovalRepository;
    private final LeaveApplicationRepository leaveApplicationRepository;
    private final StatusTypeRepository statusTypeRepository;

    private static final int STATUS_PENDING_ID = 1;
    private static final int STATUS_APPROVED_ID = 2;
    private static final int STATUS_REJECTED_ID = 3;
    private static final int STATUS_CANCELED_ID = 4;
    private static final int STATUS_WAITING_ID = 5;

    // 獲取待審核列表
    @Transactional(readOnly = true)
    public List<PendingApprovalDTO> getPendingApprovals(Integer approverId) {
        return leaveApprovalRepository.findPendingApprovalsByApproverId(approverId);
    }

    // 處理審核操作
    @Transactional
    public void processApproval(Integer approvalStepId, String action, String comments, Integer managerId) {
        // 1. 查找審核步驟，並進行前置檢查
        LeaveApproval step = leaveApprovalRepository.findById(approvalStepId)
                .orElseThrow(() -> new IllegalArgumentException("找不到該審核步驟 (ID: " + approvalStepId + ")"));

        if (!step.getApprover().getId().equals(managerId)) {
            throw new SecurityException("您沒有權限審核此項目");
        }

        if (step.getStatusType().getId() != STATUS_PENDING_ID) {
            throw new IllegalStateException("此項目已被審核過，請勿重複操作");
        }

        // 2. 更新審核步驟的狀態
        boolean isApproveAction = "approve".equalsIgnoreCase(action);
        int newStatusId = isApproveAction ? STATUS_APPROVED_ID : STATUS_REJECTED_ID;
        StatusType newStatus = statusTypeRepository.findById(newStatusId)
                .orElseThrow(() -> new IllegalStateException("找不到對應的狀態ID: " + newStatusId));

        step.setStatusType(newStatus);
        step.setComments(comments);
        step.setApprovalDate(LocalDateTime.now());
        leaveApprovalRepository.save(step);

        // 3. 檢查並更新主假單的狀態
        LeaveApplication mainApplication = step.getLeaveApplication();

        // 如果是駁回，主假單直接變為駁回
        if (isApproveAction) {
            // --- 如果是批准 ---
            // 4a. 嘗試啟用下一關
            activateNextApprovalStep(step);

            // 4b. 檢查是否所有步驟都已批准，若是，則更新主單狀態
            List<LeaveApproval> allSteps = leaveApprovalRepository.findByLeaveApplicationId(mainApplication.getId());
            boolean allApproved = allSteps.stream()
                    .allMatch(s -> s.getStatusType().getId().equals(STATUS_APPROVED_ID));
            if (allApproved) {
                mainApplication.setStatus(newStatus); // newStatus 此時是 Approved
                leaveApplicationRepository.save(mainApplication);
            }
        } else {
            // --- 如果是駁回 ---
            // 4a. 主假單的總體狀態直接變為「已駁回」
            mainApplication.setStatus(newStatus); // newStatus 此時是 Rejected
            leaveApplicationRepository.save(mainApplication);

            // 4b. 將所有其他「等待中」或「待審核」的步驟，狀態全部更新為「已取消」
            cancelRemainingSteps(mainApplication.getId(), step.getId());
        }
    }

    private void activateNextApprovalStep(LeaveApproval completedStep) {
        int currentLevel = completedStep.getApprovalLevel();
        int nextLevel = currentLevel + 1;

        // 查找這張假單所有關聯的審核步驟
        List<LeaveApproval> allSteps = leaveApprovalRepository
                .findByLeaveApplicationId(completedStep.getLeaveApplication().getId());

        // 找到下一關 (level = currentLevel + 1)，並將其狀態從 WAITING 改為 PENDING
        allSteps.stream()
                .filter(step -> step.getApprovalLevel() == nextLevel
                        && step.getStatusType().getId().equals(STATUS_WAITING_ID))
                .findFirst()
                .ifPresent(nextStep -> {
                    StatusType pendingStatus = statusTypeRepository.findById(STATUS_PENDING_ID).get();
                    nextStep.setStatusType(pendingStatus);
                    leaveApprovalRepository.save(nextStep);
                });
    }

    private void cancelRemainingSteps(Integer applicationId, Integer currentStepId) {
        List<LeaveApproval> allSteps = leaveApprovalRepository.findByLeaveApplicationId(applicationId);
        StatusType canceledStatus = statusTypeRepository.findById(STATUS_CANCELED_ID)
                .orElseThrow(() -> new IllegalStateException("找不到 Canceled 狀態 (ID: " + STATUS_CANCELED_ID + ")"));

        allSteps.stream()
                // 篩選出不是當前步驟，且狀態還是 Pending 或 Waiting 的步驟
                .filter(step -> !step.getId().equals(currentStepId))
                .filter(step -> step.getStatusType().getId().equals(STATUS_PENDING_ID)
                        || step.getStatusType().getId().equals(STATUS_WAITING_ID))
                .forEach(stepToCancel -> {
                    stepToCancel.setStatusType(canceledStatus);
                    leaveApprovalRepository.save(stepToCancel);
                });
    }
}
