package com.example.employee_system.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_system.dto.LoginRequest;
import com.example.employee_system.dto.LoginResponse;
import com.example.employee_system.service.EmployeeService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final EmployeeService employeeService;

    public AuthController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping("/login")
    public LoginResponse login(@RequestBody LoginRequest loginRequest) {
        return employeeService.login(loginRequest);
    }
}