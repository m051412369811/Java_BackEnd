package com.example.demo.entity;

import lombok.Data;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "leave_approval")
public class LeaveApproval {

    /**
     * 主鍵 (PK)，審核紀錄的唯一ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "LeaveApprovalID")
    private Integer id;

    /**
     * 關聯的請假申請單 (FK)
     * 多筆審核步驟可以對應到一筆請假申請
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "LeaveApplicationID", nullable = false)
    private LeaveApplication leaveApplication;

    /**
     * 應審核此步驟的主管 (FK)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ApproverID", nullable = false)
    private Employee approver;

    /**
     * 審核階段 (例如: 1=部門主管, 2=高階主管)
     */
    @Column(name = "ApprovalLevel", nullable = false)
    private Integer approvalLevel;

    /**
     * 此審核步驟的狀態 (FK)，例如: Pending, Approved, Rejected
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StatusTypeID", nullable = false)
    private StatusType statusType;

    /**
     * 實際審核日期時間 (主管執行操作時才會有值)
     */
    @Column(name = "ApprovalDate", nullable = true)
    private LocalDateTime approvalDate;

    /**
     * 審核意見或備註 (主管可選填)
     */
    @Column(name = "Comments", length = 255, nullable = true)
    private String comments;

    /**
     * 紀錄建立時間 (由資料庫自動生成)
     */
    @Column(name = "CreateTime", nullable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 紀錄更新時間 (由資料庫自動更新)
     */
    @Column(name = "UpdateTime", nullable = false)
    private LocalDateTime updateTime;

    // Lombok 的 @Data 會自動產生 Getters, Setters, toString, equals, hashCode
}
