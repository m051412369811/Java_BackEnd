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
    @Column(name = "LeaveTypeID")
    private Integer id;

    @Column(name = "LeaveType", nullable = false, length = 45)
    private String type;

    @OneToMany(mappedBy = "leaveType", fetch = FetchType.LAZY)
    private List<LeaveApplication> leaveApplications;

    // getters and setters
}
