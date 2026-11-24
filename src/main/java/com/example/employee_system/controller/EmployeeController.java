
package com.example.employee_system.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;



import org.springframework.data.domain.Sort;

import jakarta.validation.Valid;

import com.example.employee_system.dto.EmployeeDTO;
import com.example.employee_system.entity.Employee;
import com.example.employee_system.service.EmployeeService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "bearerAuth")
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
@ResponseStatus(HttpStatus.CREATED)
public EmployeeDTO addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {

    Employee employee = employeeService.convertToEntity(employeeDTO);

    Employee savedEmployee = employeeService.saveEmployee(employee);

    return employeeService.convertToDTO(savedEmployee);
}

    @GetMapping
public List<EmployeeDTO> getAllEmployees() {

    return employeeService.getAllEmployees()
            .stream()
            .map(employeeService::convertToDTO)
            .toList();
}

@PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/{id}")
public EmployeeDTO getEmployeeById(@PathVariable Long id) {

    Employee employee = employeeService.getEmployeeById(id);

    return employeeService.convertToDTO(employee);
}

@PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
public EmployeeDTO updateEmployee(
        @PathVariable Long id,
        @Valid @RequestBody EmployeeDTO employeeDTO) {

    Employee employee = employeeService.convertToEntity(employeeDTO);

    Employee updatedEmployee = employeeService.updateEmployee(id, employee);

    return employeeService.convertToDTO(updatedEmployee);
}

@PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
@ResponseStatus(HttpStatus.NO_CONTENT)
public void deleteEmployee(@PathVariable Long id) {

    employeeService.deleteEmployee(id);
}

@GetMapping("/search")
public List<EmployeeDTO> searchEmployees(@RequestParam String name) {

    return employeeService.searchEmployeesByName(name)
            .stream()
            .map(employeeService::convertToDTO)
            .toList();
}

@GetMapping("/department/{department}")
public List<EmployeeDTO> searchEmployeesByDepartment(
        @PathVariable String department) {

    return employeeService.searchEmployeesByDepartment(department)
            .stream()
            .map(employeeService::convertToDTO)
            .toList();
}

@GetMapping("/salary")
public List<EmployeeDTO> searchEmployeesByMinimumSalary(
        @RequestParam Double min) {

    return employeeService.searchEmployeesByMinimumSalary(min)
            .stream()
            .map(employeeService::convertToDTO)
            .toList();
}

@GetMapping("/page")
public Page<EmployeeDTO> getEmployeesWithPagination(
        @RequestParam int page,
        @RequestParam int size,
        @RequestParam String sortBy) {

    Pageable pageable = PageRequest.of(
            page,
            size,
            Sort.by(sortBy).ascending()
    );

    return employeeService.getEmployees(pageable)
            .map(employeeService::convertToDTO);
}
}
