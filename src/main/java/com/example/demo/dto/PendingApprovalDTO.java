package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PendingApprovalDTO {

    private Integer approvalStepId; // 審核步驟的ID (主管操作時需要)
    private Integer leaveApplicationId; // 假單的ID
    private String applicantName; // 申請人姓名
    private String leaveTypeName; // 假別
    private Integer leaveDays; // 天數
    private LocalDate startDate; // 開始日期
    private LocalDate endDate; // 結束日期
    private String description; // 請假事由
    private LocalDate applyDate; // 申請日期
}
