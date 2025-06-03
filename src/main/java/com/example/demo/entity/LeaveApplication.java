package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

// 請假申請
@Entity
@Data
@Table(name = "leave_applications")
public class LeaveApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveApplicationID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employee;

    @Column(name = "ApplyDate", nullable = false)
    private Date applyDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LeaveTypeID", nullable = false)
    private LeaveType leaveType;

    @Column(name = "LeaveDay", nullable = false)
    private Integer leaveDay;

    @Column(name = "LeaveStart", nullable = false)
    private Date leaveStart;

    @Column(name = "LeaveEnd", nullable = false)
    private Date leaveEnd;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Status", nullable = false)
    private StatusType status;

    @Column(name = "CreateTime", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "UpdateTime", nullable = false)
    private LocalDateTime updateTime;

    @OneToMany(mappedBy = "leaveApplication", fetch = FetchType.LAZY)
    private List<LeaveRecord> leaveRecords;

    // getters and setters
}