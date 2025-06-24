package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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

    @ManyToMany(fetch = FetchType.EAGER) // 權限通常跟著使用者一起載入，建議用 EAGER
    @JoinTable(name = "employee_roles", // 中間表的名稱
            joinColumns = @JoinColumn(name = "employee_id"), // 在中間表中，代表 Employee 這方的欄位
            inverseJoinColumns = @JoinColumn(name = "role_id") // 在中間表中，代表另一方(Role)的欄位
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Role> roles = new HashSet<>();

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateTime;

    private LocalDateTime lastLogInTime;

    @Column(length = 64)
    private String password;

    @Column
    private String salt;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<AttendanceRecord> attendanceRecords;

    @OneToMany(mappedBy = "employee", fetch = FetchType.LAZY)
    private List<LeaveApplication> leaveApplications;
    // getters and setters
}
