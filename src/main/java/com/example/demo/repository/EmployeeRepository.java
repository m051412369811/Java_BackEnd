package com.example.demo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.dto.EmployeeSummaryDTO;
import com.example.demo.dto.OptionDTO;
import com.example.demo.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {

    // 根據部門ID查詢員工列表
    @Query("SELECT new com.example.demo.dto.EmployeeSummaryDTO(e.id, CONCAT(e.firstName,' ',e.lastName), d.departmentName, t.titleName, e.hireDate) "
            + "FROM Employee e JOIN e.department d JOIN e.title t "
            + "WHERE (:departmentId IS NULL OR d.id = :departmentId)")
    List<EmployeeSummaryDTO> findSummariesByDepartmentId(@Param("departmentId") Integer departmentId);

    @Query("SELECT new com.example.demo.dto.OptionDTO(e.id, CONCAT(e.firstName,' ',e.lastName)) " +
            "FROM Employee e JOIN e.roles r " +
            "WHERE e.department.id = :departmentId " +
            "AND r.roleName = 'MANAGER' " + // 假設主管的角色名稱是 'MANAGER'
            "AND (:employeeId IS NULL OR e.id != :employeeId) " +
            "ORDER BY e.title.id DESC")
    List<OptionDTO<Integer>> findManagerOptionsByDepartment(
            @Param("departmentId") Integer departmentId,
            @Param("employeeId") Integer employeeId);
}
