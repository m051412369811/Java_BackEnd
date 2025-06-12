package com.example.demo.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.LeaveApplicationSummaryDTO;
import com.example.demo.entity.LeaveApplication;

public interface LeaveApplicationRepository extends JpaRepository<LeaveApplication, Integer> {
    @Query("SELECT new com.example.demo.dto.LeaveApplicationSummaryDTO(" +
            "la.id, CONCAT(e.lastName, e.firstName), lt.type, la.leaveDay, st.type, la.leaveStart, la.leaveEnd, la.applyDate) "
            +
            "FROM LeaveApplication la " +
            "JOIN la.employee e " +
            "JOIN la.leaveType lt " +
            "JOIN la.status st " +
            "WHERE e.id = :empId " +
            "ORDER BY la.applyDate DESC")
    List<LeaveApplicationSummaryDTO> findSummaryByEmployeeId(@Param("empId") Integer empId);

}
