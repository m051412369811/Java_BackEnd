package com.example.demo.service;

import java.security.NoSuchAlgorithmException;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.util.SecurityUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LogInService {
    private final EmployeeRepository employeeRepository;

    public Employee verifyLogin(int empId, String inputPassword) {
        // 1. 使用 findById 查詢，回傳的是 Optional<Employee>
        Optional<Employee> employeeOptional = employeeRepository.findById(empId);

        // 2. 檢查 Optional 中是否有值，判斷員工是否存在
        if (employeeOptional.isEmpty()) {
            // 找不到該員工ID，直接回傳 null
            return null;
        }

        // 3. 如果員工存在，從 Optional 中取出 Employee 物件
        Employee emp = employeeOptional.get();

        try {
            // 4. 使用資料庫中儲存的 salt，來對「使用者本次輸入的密碼」進行雜湊
            byte[] salt = emp.getSalt();
            String hashedInputPassword = SecurityUtil.hashPassword(inputPassword, salt);

            // 5. 比對雜湊後的結果，是否與資料庫中儲存的密碼一致
            if (hashedInputPassword.equals(emp.getPassword())) {
                // 密碼正確，驗證成功，回傳員工物件
                return emp;
            }
        } catch (NoSuchAlgorithmException e) {
            // 處理加密演算法不存在的例外（理論上不應發生）
            // 可以在此記錄錯誤日誌
            System.err.println("密碼雜湊演算法不存在: " + e.getMessage());
            return null;
        }

        // 密碼錯誤，回傳 null
        return null;
    }
}
