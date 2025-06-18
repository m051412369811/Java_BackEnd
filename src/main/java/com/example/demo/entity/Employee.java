package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 員工
@Entity
@Data
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 假設您的主鍵是自動遞增的
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "department_id", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "title_id", nullable = false)
    private Title title;

    @Column(nullable = false, length = 45)
    private String lastName;

    @Column(nullable = false, length = 45)
    private String firstName;

    @Column(nullable = false)
    private LocalDate hireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id") // 之前討論過，改為 manager_id 更清晰
    private Employee manager;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<Employee> subordinates;

    @Column(nullable = false)
    private LocalDate birthDate;

    @Column(name = "Role", nullable = false)
    private Boolean role;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    private LocalDateTime lastLogInTime;

    @Column(length = 64)
    private String password;

    @Column
    private byte[] salt;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<AttendanceRecord> attendanceRecords;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<LeaveApplication> leaveApplications;
    // getters and setters
}
