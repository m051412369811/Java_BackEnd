package com.example.demo.controller;

import com.example.demo.dto.BaseApiResponse;
import com.example.demo.dto.OptionDTO;
import com.example.demo.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/options")
@RequiredArgsConstructor
public class OptionsController {

    private final EmployeeService employeeService;

    /**
     * 獲取所有部門選項，用於下拉選單
     */
    @GetMapping("/departments")
    public ResponseEntity<BaseApiResponse<List<OptionDTO<Integer>>>> getDepartmentOptions() {
        List<OptionDTO<Integer>> options = employeeService.getAllDepartmentOptions();
        return ResponseEntity.ok(new BaseApiResponse<>(options));
    }

    /**
     * 獲取所有職稱選項，用於下拉選單
     */
    @GetMapping("/titles")
    public ResponseEntity<BaseApiResponse<List<OptionDTO<Integer>>>> getTitleOptions() {
        List<OptionDTO<Integer>> options = employeeService.getAllTitleOptions();
        return ResponseEntity.ok(new BaseApiResponse<>(options));
    }

    /**
     * 獲取所有可作為主管的員工選項，用於下拉選單
     */
    @GetMapping("/managers")
    public ResponseEntity<BaseApiResponse<List<OptionDTO<Integer>>>> getManagerOptions() {
        List<OptionDTO<Integer>> options = employeeService.getAllManagerOptions();
        return ResponseEntity.ok(new BaseApiResponse<>(options));
    }
}
