package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

// 請假紀錄
@Entity
@Data
@Table(name = "leave_records")
public class LeaveRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveRecordID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LeaveApplicationID", nullable = false)
    private LeaveApplication leaveApplication;

    @Column(name = "ApprovedDate", nullable = false)
    private LocalDate approvedDate;

    @Column(name = "CreateTime", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "UpdateTime", nullable = false)
    private LocalDateTime updateTime;
}
