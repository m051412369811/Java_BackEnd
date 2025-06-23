package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.service.LogInService;
import com.example.demo.dto.*;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LogInController {

    private final LogInService loginService;

    // 登入
    @PostMapping("/login")
    public BaseApiResponse<LogInUserDTO> login(
            @RequestParam int empId,
            @RequestParam String password,
            HttpSession session) {

        Employee emp = loginService.verifyLogin(empId, password);
        if (emp != null) {
            // 存session
            session.setAttribute("EMPLOYEE_ID", emp.getId());

            // 封裝 DTO（避免 Entity 洩漏）
            LogInUserDTO dto = new LogInUserDTO(emp.getId(), emp.getLastName() + emp.getFirstName());
            return new BaseApiResponse<>(dto);
        } else {
            return new BaseApiResponse<>("員工編號或密碼錯誤");
        }
    }

    // 查詢當前登入者
    @GetMapping("/user")
    public BaseApiResponse<LogInUserDTO> userInfo(HttpSession session) {
        Object empId = session.getAttribute("EMPLOYEE_ID");
        if (empId != null) {
            // 你可以再用 empId 查一次 DB 拿名稱，也可以只給 id
            // 這裡假設你不查 DB，直接給 id
            LogInUserDTO dto = new LogInUserDTO((Integer) empId, null);
            return new BaseApiResponse<>(dto);
        } else {
            return new BaseApiResponse<>("尚未登入");
        }
    }

    // 登出
    @PostMapping("/logout")
    public BaseApiResponse<Void> logout(HttpSession session) {
        session.invalidate();
        return new BaseApiResponse<>((Void) null);
    }
}
