package com.example.employee_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee_system.entity.Department;

public interface DepartmentRepository extends JpaRepository<Department, Long> {

}