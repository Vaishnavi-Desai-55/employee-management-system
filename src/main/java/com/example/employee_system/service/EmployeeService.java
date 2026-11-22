package com.example.employee_system.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.employee_system.dto.EmployeeDTO;
import com.example.employee_system.dto.LoginRequest;
import com.example.employee_system.dto.LoginResponse;
import com.example.employee_system.entity.Employee;
import com.example.employee_system.exception.EmployeeNotFoundException;
import com.example.employee_system.repository.EmployeeRepository;

import com.example.employee_system.enums.Role;

import com.example.employee_system.security.JwtService;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public EmployeeService(EmployeeRepository employeeRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
    this.employeeRepository = employeeRepository;
    this.passwordEncoder = passwordEncoder;
    this.jwtService = jwtService;
}

    // Save Employee
    public Employee saveEmployee(Employee employee) {

        employee.setPassword(
                passwordEncoder.encode(employee.getPassword())
        );

        return employeeRepository.save(employee);
    }

    // Get All Employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get Employee By ID
    public Employee getEmployeeById(Long id) {

        return employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));
    }

    // Update Employee
    public Employee updateEmployee(Long id, Employee employee) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new EmployeeNotFoundException(id));

        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setSalary(employee.getSalary());

        return employeeRepository.save(existingEmployee);
    }

    // Delete Employee
    public void deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }

        employeeRepository.deleteById(id);
    }

    // Search Employees By Name
    public List<Employee> searchEmployeesByName(String name) {

        return employeeRepository.findByNameContainingIgnoreCase(name);
    }

    // Search Employees By Department
    public List<Employee> searchEmployeesByDepartment(String department) {

        return employeeRepository.findByDepartmentIgnoreCase(department);
    }

    // Search Employees By Minimum Salary
    public List<Employee> searchEmployeesByMinimumSalary(Double salary) {

        return employeeRepository.findBySalaryGreaterThanEqual(salary);
    }

    // Pagination
    public Page<Employee> getEmployees(Pageable pageable) {

        return employeeRepository.findAll(pageable);
    }

    // Convert Employee to DTO
    public EmployeeDTO convertToDTO(Employee employee) {

        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPassword(),
                employee.getRole(),
                employee.getDepartment(),
                employee.getSalary()
        );
    }

    // Convert DTO to Employee
    public Employee convertToEntity(EmployeeDTO dto) {

        Employee employee = new Employee();

        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());
        employee.setRole(dto.getRole());
        employee.setDepartment(dto.getDepartment());
        employee.setSalary(dto.getSalary());

        return employee;
    }

    // Employee Login
    public LoginResponse login(LoginRequest loginRequest) {

        Employee employee = employeeRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(
                loginRequest.getPassword(),
                employee.getPassword())) {

            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtService.generateToken(employee);

return new LoginResponse(
        "Login successful",
        token,
        employee.getId(),
        employee.getName(),
        employee.getRole().name()
);
    }
}