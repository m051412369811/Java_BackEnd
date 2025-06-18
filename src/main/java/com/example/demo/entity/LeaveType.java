package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

// 請假類型
@Entity
@Data
@Table(name = "leave_types")
public class LeaveType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 45)
    private String typeName; // 原為 type，改為 typeName 更清晰

    @OneToMany(mappedBy = "leaveType", fetch = FetchType.LAZY)
    private List<LeaveApplication> leaveApplications;

    // getters and setters
}
