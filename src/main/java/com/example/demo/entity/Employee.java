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
    @Column(name = "EmployeeID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DepartmentID", nullable = false)
    private Department department;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TitleID", nullable = false)
    private Title title;

    @Column(name = "LastName", nullable = false, length = 45)
    private String lastName;

    @Column(name = "FirstName", nullable = false, length = 45)
    private String firstName;

    @Column(name = "HireDate", nullable = false)
    private LocalDate hireDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ReportsTo")
    private Employee manager;

    @OneToMany(mappedBy = "manager", fetch = FetchType.LAZY)
    private List<Employee> subordinates;

    @Column(name = "BirthDate", nullable = false)
    private LocalDate birthDate;

    @Column(name = "Role", nullable = false)
    private Boolean role;

    @Column(name = "CreateTime", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "UpdateTime", nullable = false)
    private LocalDateTime updateTime;

    @Column(name = "LastLogInTime")
    private LocalDateTime lastLogInTime;

    @Column(name = "Password", length = 64)
    private String password;

    @Column(name = "Salt")
    private byte[] salt;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<AttendanceRecord> attendanceRecords;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<LeaveApplication> leaveApplications;

    // getters and setters
}
