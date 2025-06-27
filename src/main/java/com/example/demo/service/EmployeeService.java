package com.example.demo.service;

import com.example.demo.dto.EmployeeRequestDTO;
import com.example.demo.dto.EmployeeSummaryDTO;
import com.example.demo.dto.OptionDTO;
import com.example.demo.entity.*;
import com.example.demo.repository.*;
import com.example.demo.util.SecurityUtil;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    // 將所有依賴宣告為 final，由建構子注入
    private final EmployeeRepository employeeRepository;
    private final DepartmentRepository departmentRepository;
    private final TitleRepository titleRepository;
    private final RoleRepository roleRepository;

    // 根據部門ID獲取員工列表。如果 departmentId 為 null，則查詢所有員工。

    @Transactional(readOnly = true)
    public List<EmployeeSummaryDTO> getEmployeesByDepartment(Integer departmentId) {
        return employeeRepository.findSummariesByDepartmentId(departmentId);
    }

    // 新增員工
    @Transactional
    public Employee createEmployee(EmployeeRequestDTO dto) {
        // 1. 根據傳入的 ID 查找關聯的 Entity
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("無效的部門ID: " + dto.getDepartmentId()));

        Title title = titleRepository.findById(dto.getTitleId())
                .orElseThrow(() -> new IllegalArgumentException("無效的職稱ID: " + dto.getTitleId()));

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
        if (roles.size() != dto.getRoleIds().size()) {
            throw new IllegalArgumentException("提供的一個或多個角色ID無效");
        }

        // 只有在 managerId 存在時才去查詢主管
        Employee manager = null;
        if (dto.getManagerId() != null) {
            manager = employeeRepository.findById(dto.getManagerId())
                    .orElseThrow(() -> new IllegalArgumentException("無效的主管ID: " + dto.getManagerId()));
        }

        // 2. 建立並組裝新的 Employee Entity
        Employee employee = new Employee();
        employee.setLastName(dto.getLastName());
        employee.setFirstName(dto.getFirstName());
        employee.setHireDate(dto.getHireDate());
        employee.setDepartment(department);
        employee.setTitle(title);
        employee.setManager(manager);
        employee.setRoles(roles);

        // 確保 birthDate 有被正確設定
        employee.setBirthDate(dto.getBirthDate());

        // 3. 處理預設密碼
        String defaultPassword = dto.getBirthDate().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        try {
            String salt = SecurityUtil.generateSalt();
            String hashedPassword = SecurityUtil.hashPassword(defaultPassword, salt);
            employee.setPassword(hashedPassword);
            employee.setSalt(salt);
        } catch (NoSuchAlgorithmException e) {
            // 這個例外在標準 Java 環境中幾乎不可能發生
            throw new RuntimeException("密碼加密服務初始化失敗", e);
        }

        // 4. 儲存並回傳
        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Integer employeeId, EmployeeRequestDTO dto) {
        // 1. 查找要更新的員工
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("找不到要更新的員工ID: " + employeeId));

        // 2. 查找關聯的 Entity
        Department department = departmentRepository.findById(dto.getDepartmentId())
                .orElseThrow(() -> new IllegalArgumentException("找不到要部門"));
        Title title = titleRepository.findById(dto.getTitleId())
                .orElseThrow(() -> new IllegalArgumentException("找不到要職位"));
        Employee manager = (dto.getManagerId() != null) ? employeeRepository.findById(dto.getManagerId()).orElse(null)
                : null;

        Set<Role> roles = new HashSet<>(roleRepository.findAllById(dto.getRoleIds()));
        if (roles.size() != dto.getRoleIds().size()) {
            throw new IllegalArgumentException("提供的一個或多個角色ID無效");
        }

        // 3. 更新欄位
        employee.setLastName(dto.getLastName());
        employee.setFirstName(dto.getFirstName());
        employee.setBirthDate(dto.getBirthDate());
        employee.setHireDate(dto.getHireDate());
        employee.setDepartment(department);
        employee.setTitle(title);
        employee.setManager(manager);
        employee.setRoles(roles);

        return employeeRepository.save(employee);
    }

    /**
     * 更新員工資料的邏輯 (簡化版，您可以後續擴充)
     */
    @Transactional(readOnly = true)
    public EmployeeRequestDTO getEmployeeForEdit(Integer employeeId) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("找不到員工ID: " + employeeId));

        // 手動將 Entity 轉換為 RequestDTO
        EmployeeRequestDTO dto = new EmployeeRequestDTO();
        dto.setLastName(employee.getLastName());
        dto.setFirstName(employee.getFirstName());
        dto.setBirthDate(employee.getBirthDate());
        dto.setHireDate(employee.getHireDate());
        dto.setDepartmentId(employee.getDepartment().getId());
        dto.setTitleId(employee.getTitle().getId());
        if (employee.getManager() != null) {
            dto.setManagerId(employee.getManager().getId());
        }

        return dto;
    }

    // --- 用於提供前端下拉選單的資料 ---

    @Transactional(readOnly = true)
    public List<OptionDTO<Integer>> getAllDepartmentOptions() {
        return departmentRepository.findAllAsOptions();
    }

    @Transactional(readOnly = true)
    public List<OptionDTO<Integer>> getAllTitleOptions() {
        return titleRepository.findAllAsOptions();
    }

    @Transactional(readOnly = true)
    public List<OptionDTO<Integer>> getManagerOptions(Integer departmentId, Integer employeeId) {
        if (departmentId == null) {
            return Collections.emptyList(); // 如果沒有部門ID，則不提供任何主管選項
        }
        return employeeRepository.findManagerOptionsByDepartment(departmentId, employeeId);
    }
}