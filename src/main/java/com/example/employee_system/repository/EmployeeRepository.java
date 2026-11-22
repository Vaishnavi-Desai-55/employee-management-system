package com.example.employee_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee_system.entity.Employee;

import java.util.Optional;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    List<Employee> findByNameContainingIgnoreCase(String name);
    List<Employee> findByDepartmentIgnoreCase(String department);
    List<Employee> findBySalaryGreaterThanEqual(Double salary);
    Optional<Employee> findByEmail(String email);
}