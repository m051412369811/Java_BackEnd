package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.util.SecurityUtil;

@Service
public class LogInService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Employee verifyLogin(int empId, String inputPassword) {
        Employee emp = employeeRepository.findById(empId);
        if (emp == null)
            return null;
        String hashedInput = SecurityUtil.hashPassword(inputPassword, emp.getSalt());
        if (hashedInput.equals(emp.getPassword())) {
            return emp;
        }
        return null;
    }
}
