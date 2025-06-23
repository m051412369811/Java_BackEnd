package com.example.demo.dto;

import jakarta.validation.constraints.*;
import java.time.LocalDate;
import lombok.Data;

@Data
public class EmployeeRequestDTO {
    @NotBlank(message = "姓氏不得為空")
    private String lastName;

    @NotBlank(message = "名字不得為空")
    private String firstName;

    @NotNull(message = "生日不得為空")
    private LocalDate birthDate;

    @NotNull(message = "到職日不得為空")
    private LocalDate hireDate;

    @NotNull(message = "部門為必填")
    private Integer departmentId;

    @NotNull(message = "職稱為必填")
    private Integer titleId;

    // 直屬主管可以是 null (例如 CEO)
    private Integer managerId;

}
