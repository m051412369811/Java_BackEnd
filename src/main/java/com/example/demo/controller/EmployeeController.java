package com.example.demo.controller;

import com.example.demo.dto.BaseApiResponse;
import com.example.demo.dto.EmployeeRequestDTO;
import com.example.demo.dto.EmployeeSummaryDTO;
import com.example.demo.entity.Employee;
import com.example.demo.service.EmployeeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<BaseApiResponse<List<EmployeeSummaryDTO>>> getEmployees(
            @RequestParam(required = false) Integer departmentId) {
        try {
            List<EmployeeSummaryDTO> employees = employeeService.getEmployeesByDepartment(departmentId);
            // ✅ 查詢成功，回傳 200 OK
            return ResponseEntity.ok(new BaseApiResponse<>(employees));
        } catch (Exception e) {
            // ✅ 若發生非預期錯誤，回傳 500 Internal Server Error
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse<>("查詢員工列表時發生錯誤"));
        }
    }

    @PostMapping
    public ResponseEntity<BaseApiResponse<Integer>> createEmployee(@Valid @RequestBody EmployeeRequestDTO dto) {
        // ✅ 在這裡印出日誌來檢查 DTO 的內容
        System.out.println("收到的 DTO 內容: " + dto.toString());

        try {
            Employee createdEmployee = employeeService.createEmployee(dto);
            // ✅ 新增成功，回傳 201 Created，並在 body 中附上新員工的 ID
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(new BaseApiResponse<>(createdEmployee.getId()));
        } catch (IllegalArgumentException e) {
            // ✅ 對於已知的業務邏輯錯誤（如ID不存在），回傳 400 Bad Request
            return ResponseEntity.badRequest().body(new BaseApiResponse<>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse<>("新增員工時發生未預期的錯誤"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseApiResponse<Integer>> updateEmployee(
            @PathVariable Integer id,
            @Valid @RequestBody EmployeeRequestDTO dto) {
        try {
            Employee updatedEmployee = employeeService.updateEmployee(id, dto);
            // ✅ 更新成功，回傳 200 OK
            return ResponseEntity.ok(new BaseApiResponse<>(updatedEmployee.getId()));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(new BaseApiResponse<>(e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new BaseApiResponse<>("更新員工資料時發生未預期的錯誤"));
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseApiResponse<EmployeeRequestDTO>> getEmployeeById(@PathVariable Integer id) {
        try {
            EmployeeRequestDTO employee = employeeService.getEmployeeForEdit(id);
            return ResponseEntity.ok(new BaseApiResponse<>(employee));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new BaseApiResponse<>(e.getMessage()));
        }
    }
}
