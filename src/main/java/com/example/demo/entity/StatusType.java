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
    @Column(name = "StatusTypeID")
    private Integer id;

    @Column(name = "StatusType", nullable = false, length = 45)
    private String type;

    @OneToMany(mappedBy = "status", fetch = FetchType.LAZY)
    private List<LeaveApplication> leaveApplications;

    // getters and setters
}
