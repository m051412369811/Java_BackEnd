package com.example.demo.controller;

import com.example.demo.dto.ApprovalActionRequestDTO;
import com.example.demo.dto.BaseApiResponse;
import com.example.demo.dto.PendingApprovalDTO;
import com.example.demo.service.ApprovalService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/approvals")
@RequiredArgsConstructor
public class ApprovalController {

    private final ApprovalService approvalService;

    @GetMapping("/pending")
    public ResponseEntity<BaseApiResponse<List<PendingApprovalDTO>>> getPendingApprovals(HttpSession session) {
        Integer managerId = (Integer) session.getAttribute("EMPLOYEE_ID");
        if (managerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseApiResponse<>("尚未登入"));
        }

        List<PendingApprovalDTO> pendingList = approvalService.getPendingApprovals(managerId);
        return ResponseEntity.ok(new BaseApiResponse<>(pendingList));
    }

    @PostMapping("/{stepId}/{action}")
    public ResponseEntity<BaseApiResponse<Object>> handleApproval(
            @PathVariable Integer stepId,
            @PathVariable String action, // "approve" or "reject"
            @RequestBody(required = false) ApprovalActionRequestDTO dto, // 評論可選
            HttpSession session) {

        Integer managerId = (Integer) session.getAttribute("EMPLOYEE_ID");
        if (managerId == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseApiResponse<>("尚未登入"));
        }

        if (!("approve".equalsIgnoreCase(action) || "reject".equalsIgnoreCase(action))) {
            return ResponseEntity.badRequest().body(new BaseApiResponse<>("無效的操作"));
        }

        try {
            String comments = (dto != null) ? dto.getComments() : null;
            approvalService.processApproval(stepId, action, comments, managerId);
            return ResponseEntity.ok(new BaseApiResponse<>((Object) "審核操作成功"));
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(new BaseApiResponse<>(e.getMessage()));
        } catch (IllegalStateException | IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BaseApiResponse<>(e.getMessage()));
        }
    }
}