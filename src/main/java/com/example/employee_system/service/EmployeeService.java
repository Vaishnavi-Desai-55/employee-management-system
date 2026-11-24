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
import com.example.employee_system.security.JwtService;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public EmployeeService(
            EmployeeRepository employeeRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    // Add employee
    public Employee saveEmployee(Employee employee) {

        if (employee.getPassword() == null ||
            employee.getPassword().isBlank()) {

            throw new IllegalArgumentException(
                    "Password is required when creating an employee");
        }

        employee.setPassword(
                passwordEncoder.encode(employee.getPassword())
        );

        return employeeRepository.save(employee);
    }

    // Get all employees
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    // Get employee by ID
    public Employee getEmployeeById(Long id) {

        return employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(id));
    }

    // Update employee
    public Employee updateEmployee(Long id, Employee employee) {

        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(id));

        existingEmployee.setName(employee.getName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setDesignation(employee.getDesignation());
        existingEmployee.setSalary(employee.getSalary());

        // Update password only when a new password is provided
        if (employee.getPassword() != null &&
            !employee.getPassword().isBlank()) {

            existingEmployee.setPassword(
                    passwordEncoder.encode(employee.getPassword())
            );
        }

        return employeeRepository.save(existingEmployee);
    }

    // Delete employee
    public void deleteEmployee(Long id) {

        if (!employeeRepository.existsById(id)) {
            throw new EmployeeNotFoundException(id);
        }

        employeeRepository.deleteById(id);
    }

    // Search employees by name
    public List<Employee> searchEmployeesByName(String name) {

        return employeeRepository
                .findByNameContainingIgnoreCase(name);
    }

    // Search employees by department
    public List<Employee> searchEmployeesByDepartment(String department) {

        return employeeRepository
                .findByDepartmentIgnoreCase(department);
    }

    // Search employees by minimum salary
    public List<Employee> searchEmployeesByMinimumSalary(Double salary) {

        return employeeRepository
                .findBySalaryGreaterThanEqual(salary);
    }

    // Pagination
    public Page<Employee> getEmployees(Pageable pageable) {

        return employeeRepository.findAll(pageable);
    }

    // Convert Employee to EmployeeDTO
    public EmployeeDTO convertToDTO(Employee employee) {

        return new EmployeeDTO(
                employee.getId(),
                employee.getName(),
                employee.getEmail(),
                employee.getPassword(),
                employee.getRole(),
                employee.getDepartment(),
                employee.getDesignation(),
                employee.getSalary()
        );
    }

    // Convert EmployeeDTO to Employee
    public Employee convertToEntity(EmployeeDTO dto) {

        Employee employee = new Employee();

        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setPassword(dto.getPassword());
        employee.setRole(dto.getRole());
        employee.setDepartment(dto.getDepartment());
        employee.setDesignation(dto.getDesignation());
        employee.setSalary(dto.getSalary());

        return employee;
    }

    // Login
    public LoginResponse login(LoginRequest loginRequest) {

        Employee employee = employeeRepository
                .findByEmail(loginRequest.getEmail())
                .orElseThrow(() ->
                        new RuntimeException("Invalid email or password"));

        // Prevent NULL or blank passwords from being checked
        if (employee.getPassword() == null ||
            employee.getPassword().isBlank()) {

            throw new RuntimeException("Invalid email or password");
        }

        // Verify entered password against BCrypt hash
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