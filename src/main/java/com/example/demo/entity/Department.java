package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

// 部門
@Entity
@Data
@Table(name = "departments")
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String departmentName; // 對應到 department_name

    @OneToMany(mappedBy = "department", fetch = FetchType.LAZY)
    private List<Employee> employees;
    // getters and setters
}