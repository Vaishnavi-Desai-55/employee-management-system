package com.example.employee_system.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee_system.entity.Employee;
import com.example.employee_system.entity.Leave;

public interface LeaveRepository extends JpaRepository<Leave, Long> {

    List<Leave> findByEmployee(Employee employee);

    List<Leave> findByStatus(String status);
}