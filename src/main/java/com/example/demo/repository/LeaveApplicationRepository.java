package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.LeaveApplicationSummaryDTO;
import com.example.demo.entity.LeaveApplication;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {
    @Query("SELECT new com.example.demo.dto.LeaveApplicationSummaryDTO(" +
            "la.id, " +
            "CONCAT(e.lastName,' ',e.firstName), " +
            "lt.typeName, " + // ✅ 修正: lt.type -> lt.typeName
            "la.leaveDays, " + // ✅ 修正: la.leaveDay -> la.leaveDays
            "st.statusName, " + // ✅ 修正: st.type -> st.statusName
            "la.startDate, " + // ✅ 修正: la.leaveStart -> la.startDate
            "la.endDate, " + // ✅ 修正: la.leaveEnd -> la.endDate
            "la.applyDate, " +
            "la.description) " + // 註：這裡的參數數量和順序，需與 DTO 建構子完全匹配
            "FROM LeaveApplication la " +
            "JOIN la.employee e " +
            "JOIN la.leaveType lt " +
            "JOIN la.status st " + // 注意: LeaveApplication Entity 中的欄位是 status
            "WHERE e.id = :empId " +
            "ORDER BY la.createTime DESC")
    List<LeaveApplicationSummaryDTO> findSummaryByEmployeeId(@Param("empId") Integer empId);

}
