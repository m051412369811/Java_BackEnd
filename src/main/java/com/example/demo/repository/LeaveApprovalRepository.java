package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.LeaveApproval;

public interface LeaveApprovalRepository extends JpaRepository<LeaveApproval, Integer> {

    // 根據假單ID，查詢其所有的審核步驟
    List<LeaveApproval> findByLeaveApplicationId(Integer leaveApplicationId);
}
