package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.LocalDateTime;
import lombok.Data;

// 打卡紀錄
@Data
@Entity
@Table(name = "attendance_records")
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "AttendanceRecordID")
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "EmployeeID", nullable = false)
    private Employee employee;

    @Column(name = "Date", nullable = false)
    private LocalDate date;

    @Column(name = "ClockInTime", nullable = false)
    private LocalTime clockInTime;

    @Column(name = "ClockOutTime", nullable = false)
    private LocalTime clockOutTime;

    @Column(name = "CreateTime", nullable = false)
    private LocalDateTime createTime;

    @Column(name = "UpdateTime", nullable = false)
    private LocalDateTime updateTime;

    // getters and setters
}
