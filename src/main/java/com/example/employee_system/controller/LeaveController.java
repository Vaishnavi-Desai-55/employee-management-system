package com.example.employee_system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.employee_system.dto.LeaveDTO;
import com.example.employee_system.entity.Leave;
import com.example.employee_system.service.LeaveService;

import jakarta.validation.Valid;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/employees")
@SecurityRequirement(name = "bearerAuth")
public class LeaveController {

    private final LeaveService leaveService;

    public LeaveController(LeaveService leaveService) {
        this.leaveService = leaveService;
    }

    // Employee can apply for leave
    @PostMapping("/apply/{employeeId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('MANAGER') or hasRole('ADMIN')")
    public LeaveDTO applyLeave(
            @PathVariable Long employeeId,
            @Valid @RequestBody Leave leave) {

        return leaveService.applyLeave(employeeId, leave);
    }

    // Admin, Manager and Employee can view employee leaves
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('EMPLOYEE')")
    public List<LeaveDTO> getLeavesByEmployee(
            @PathVariable Long employeeId) {

        return leaveService.getLeavesByEmployee(employeeId);
    }

    // Only Admin and Manager can view leaves by status
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public List<LeaveDTO> getLeavesByStatus(
            @PathVariable String status) {

        return leaveService.getLeavesByStatus(status);
    }

    // Only Admin and Manager can approve/reject leave
    @PutMapping("/{leaveId}/status")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public LeaveDTO updateLeaveStatus(
            @PathVariable Long leaveId,
            @RequestParam String status) {

        return leaveService.updateLeaveStatus(leaveId, status);
    }
}