package com.example.demo.controller;

import com.example.demo.entity.Employee;
import com.example.demo.entity.Role;
import com.example.demo.service.LogInService;
import com.example.demo.dto.*;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<BaseApiResponse<LogInUserDTO>> login(
            @RequestParam int empId,
            @RequestParam String password,
            HttpSession session) {

        Employee emp = loginService.verifyLogin(empId, password);
        if (emp != null) {
            // --- ✅ 核心修改點 ---
            // 1. 從 Employee 物件中獲取 Role 集合
            Set<Role> userRoles = emp.getRoles();

            // 2. 將 Set<Role> 轉換為 Set<String>，只儲存角色的名稱
            Set<String> roleNames = userRoles.stream()
                    .map(Role::getRoleName)
                    .collect(Collectors.toSet());

            // 3. 將重要資訊存入 Session
            session.setAttribute("EMPLOYEE_ID", emp.getId());
            session.setAttribute("EMPLOYEE_NAME", emp.getFirstName() + " " + emp.getLastName());
            session.setAttribute("EMPLOYEE_ROLES", roleNames);

            // 4. 封裝包含角色資訊的 DTO 回傳給前端
            LogInUserDTO dto = new LogInUserDTO(emp.getId(), emp.getLastName() + " " + emp.getFirstName(), roleNames);
            return ResponseEntity.ok(new BaseApiResponse<>(dto));
        } else {
            return ResponseEntity.badRequest().body(new BaseApiResponse<>("員工編號或密碼錯誤"));
        }
    }

    // 查詢當前登入者
    @GetMapping("/user")
    public ResponseEntity<BaseApiResponse<LogInUserDTO>> userInfo(HttpSession session) {
        Integer empId = (Integer) session.getAttribute("EMPLOYEE_ID");

        if (empId != null) {
            // ✅ 從 Session 中讀取所有已儲存的資訊
            String name = (String) session.getAttribute("EMPLOYEE_NAME");
            Set<String> roles = (Set<String>) session.getAttribute("EMPLOYEE_ROLES");

            LogInUserDTO dto = new LogInUserDTO(empId, name, roles);
            return ResponseEntity.ok(new BaseApiResponse<>(dto));
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new BaseApiResponse<>("尚未登入"));
        }
    }

    // 登出
    @PostMapping("/logout")
    public ResponseEntity<BaseApiResponse<Object>> logout(HttpSession session) {
        session.invalidate();
        return ResponseEntity.ok(new BaseApiResponse<>(null));
    }
}
