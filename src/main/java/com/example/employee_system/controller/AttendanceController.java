package com.example.employee_system.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.example.employee_system.dto.AttendanceDTO;
import com.example.employee_system.service.AttendanceService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/attendance")
@SecurityRequirement(name = "bearerAuth")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    // Employee and Admin can check in
    @PostMapping("/check-in/{employeeId}")
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public AttendanceDTO checkIn(
            @PathVariable Long employeeId) {

        return attendanceService.checkIn(employeeId);
    }

    // Employee and Admin can check out
    @PutMapping("/check-out/{employeeId}")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public AttendanceDTO checkOut(
            @PathVariable Long employeeId) {

        return attendanceService.checkOut(employeeId);
    }

    // Admin, Manager and Employee can view attendance
    @GetMapping("/employee/{employeeId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER') or hasRole('EMPLOYEE')")
    public List<AttendanceDTO> getAttendanceByEmployee(
            @PathVariable Long employeeId) {

        return attendanceService.getAttendanceByEmployee(employeeId);
    }

    // Admin and Manager can view date-wise attendance reports
    @GetMapping("/date/{date}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('MANAGER')")
    public List<AttendanceDTO> getAttendanceByDate(
            @PathVariable LocalDate date) {

        return attendanceService.getAttendanceByDate(date);
    }
}