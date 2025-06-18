package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

// 狀態類型
@Entity
@Data
@Table(name = "status_types")
public class StatusType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String statusName; // 原為 type，改為 statusName 更清晰

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private List<LeaveApplication> leaveApplications;

    // getters and setters
}
