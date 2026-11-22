package com.example.employee_system.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.employee_system.dto.LeaveDTO;
import com.example.employee_system.entity.Employee;
import com.example.employee_system.entity.Leave;
import com.example.employee_system.exception.AttendanceException;
import com.example.employee_system.exception.EmployeeNotFoundException;
import com.example.employee_system.repository.EmployeeRepository;
import com.example.employee_system.repository.LeaveRepository;

@Service
public class LeaveService {

    private final LeaveRepository leaveRepository;
    private final EmployeeRepository employeeRepository;

    public LeaveService(
            LeaveRepository leaveRepository,
            EmployeeRepository employeeRepository) {

        this.leaveRepository = leaveRepository;
        this.employeeRepository = employeeRepository;
    }

    // Check whether the logged-in employee can access the requested employee ID
    private void validateEmployeeAccess(Long employeeId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        Employee loggedInEmployee = employeeRepository
                .findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Logged-in employee not found"));

        // Admin can access any employee
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        // Employee cannot access another employee's leave
        if (!isAdmin && !loggedInEmployee.getId().equals(employeeId)) {

            throw new AttendanceException(
                    "You are not allowed to access another employee's leave");
        }
    }

    public LeaveDTO applyLeave(Long employeeId, Leave leave) {

        validateEmployeeAccess(employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(employeeId));

        leave.setEmployee(employee);
        leave.setStatus("PENDING");

        Leave savedLeave = leaveRepository.save(leave);

        return convertToDTO(savedLeave);
    }

    public List<LeaveDTO> getLeavesByEmployee(Long employeeId) {

        validateEmployeeAccess(employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new EmployeeNotFoundException(employeeId));

        return leaveRepository.findByEmployee(employee)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<LeaveDTO> getLeavesByStatus(String status) {

        return leaveRepository.findByStatus(status)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public LeaveDTO updateLeaveStatus(Long leaveId, String status) {

    Leave leave = leaveRepository.findById(leaveId)
            .orElseThrow(() ->
                    new RuntimeException(
                            "Leave not found with id: " + leaveId));

    String normalizedStatus = status.toUpperCase();

    if (!normalizedStatus.equals("PENDING")
            && !normalizedStatus.equals("APPROVED")
            && !normalizedStatus.equals("REJECTED")) {

        throw new RuntimeException(
                "Invalid leave status. Use PENDING, APPROVED or REJECTED");
    }

    leave.setStatus(normalizedStatus);

    Leave updatedLeave = leaveRepository.save(leave);

    return convertToDTO(updatedLeave);
}

    private LeaveDTO convertToDTO(Leave leave) {

        Employee employee = leave.getEmployee();

        return new LeaveDTO(
                leave.getId(),
                employee.getId(),
                employee.getName(),
                leave.getStartDate(),
                leave.getEndDate(),
                leave.getReason(),
                leave.getStatus()
        );
    }
}