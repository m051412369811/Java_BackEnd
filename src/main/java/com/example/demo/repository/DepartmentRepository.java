package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.demo.dto.OptionDTO;
import com.example.demo.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Integer> {
    @Query("SELECT new com.example.demo.dto.OptionDTO(d.id, d.departmentName) FROM Department d ORDER BY d.id")
    List<OptionDTO<Integer>> findAllAsOptions();

}
