package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

// 請假申請
@Entity
@Data
@Table(name = "leave_applications")
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @Column(nullable = false)
    private LocalDate applyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_type_id", nullable = false)
    private LeaveType leaveType;

    @Column(nullable = false)
    private Integer leaveDays; // leaveDay -> leaveDays

    @Column(nullable = false)
    private LocalDate startDate; // leaveStart -> startDate, 並使用 LocalDate

    @Column(nullable = false)
    private LocalDate endDate; // leaveEnd -> endDate, 並使用 LocalDate

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusType status;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateTime;

    @Column(columnDefinition = "TEXT", nullable = true)
    private String description;

    @OneToMany(mappedBy = "leaveApplication", fetch = FetchType.LAZY)
    private List<LeaveRecord> leaveRecords;

    // getters and setters
}