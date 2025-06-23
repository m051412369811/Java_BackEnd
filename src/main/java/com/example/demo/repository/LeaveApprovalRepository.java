package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.PendingApprovalDTO;
import com.example.demo.entity.LeaveApproval;

public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Integer> {

    // 根據假單ID，查詢其所有的審核步驟
    List<LeaveApproval> findByLeaveApplicationId(Integer leaveApplicationId);

    // ✅ 新增這個查詢方法
    @Query("SELECT new com.example.demo.dto.PendingApprovalDTO(" +
            "step.id, " +
            "app.id, " +
            "CONCAT(applicant.lastName, ' ', applicant.firstName), " +
            "lt.typeName, " +
            "app.leaveDays, " +
            "app.startDate, " +
            "app.endDate, " +
            "app.description, " +
            "app.applyDate) " +
            "FROM LeaveApproval step " +
            "JOIN step.leaveApplication app " +
            "JOIN app.employee applicant " +
            "JOIN app.leaveType lt " +
            "WHERE step.approver.id = :approverId AND step.statusType.id = 1 " + // 1 = Pending
            "ORDER BY app.createTime ASC")
    List<PendingApprovalDTO> findPendingApprovalsByApproverId(@Param("approverId") Integer approverId);
}
