package com.example.demo.dto;

import lombok.Data;

@Data
public class ApprovalActionRequestDTO {
    // 審核時可以附加評論，為選填
    private String comments;
}
