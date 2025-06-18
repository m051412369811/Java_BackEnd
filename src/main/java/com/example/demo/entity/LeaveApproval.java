package com.example.demo.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "leave_approval")
public class LeaveApproval {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "leave_application_id", nullable = false)
    private LeaveApplication leaveApplication;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approver_id", nullable = false)
    private Employee approver;

    @Column(nullable = false)
    private Integer approvalLevel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "status_id", nullable = false)
    private StatusType statusType;

    @Column(nullable = true)
    private LocalDateTime approvalDate;

    @Column(length = 255, nullable = true)
    private String comments;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(nullable = false)
    private LocalDateTime updateTime;

    // Lombok 的 @Data 會自動產生 Getters, Setters, toString, equals, hashCode
}
