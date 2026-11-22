package com.example.employee_system.service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.example.employee_system.dto.AttendanceDTO;
import com.example.employee_system.entity.Attendance;
import com.example.employee_system.entity.Employee;
import com.example.employee_system.exception.AttendanceException;
import com.example.employee_system.repository.AttendanceRepository;
import com.example.employee_system.repository.EmployeeRepository;

@Service
public class AttendanceService {

    private final AttendanceRepository attendanceRepository;
    private final EmployeeRepository employeeRepository;

    public AttendanceService(
            AttendanceRepository attendanceRepository,
            EmployeeRepository employeeRepository) {

        this.attendanceRepository = attendanceRepository;
        this.employeeRepository = employeeRepository;
    }

    // Check whether logged-in employee is accessing their own record
    private void validateEmployeeAccess(Long employeeId) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String loggedInEmail = authentication.getName();

        Employee loggedInEmployee = employeeRepository
                .findByEmail(loggedInEmail)
                .orElseThrow(() ->
                        new RuntimeException("Logged-in employee not found"));

        // Admin can access any employee
        boolean isAdmin = authentication.getAuthorities()
                .stream()
                .anyMatch(authority ->
                        authority.getAuthority().equals("ROLE_ADMIN"));

        if (!isAdmin && !loggedInEmployee.getId().equals(employeeId)) {

            throw new AttendanceException(
                    "You are not allowed to access another employee's attendance");
        }
    }

    public AttendanceDTO checkIn(Long employeeId) {

        validateEmployeeAccess(employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with id: " + employeeId));

        LocalDate today = LocalDate.now();

        if (attendanceRepository
                .findByEmployeeAndAttendanceDate(employee, today)
                .isPresent()) {

            throw new AttendanceException(
                    "Employee has already checked in today");
        }

        Attendance attendance = new Attendance();

        attendance.setEmployee(employee);
        attendance.setAttendanceDate(today);
        attendance.setCheckIn(LocalDateTime.now());

        Attendance savedAttendance =
                attendanceRepository.save(attendance);

        return convertToDTO(savedAttendance);
    }

    public AttendanceDTO checkOut(Long employeeId) {

        validateEmployeeAccess(employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with id: " + employeeId));

        LocalDate today = LocalDate.now();

        Attendance attendance = attendanceRepository
                .findByEmployeeAndAttendanceDate(employee, today)
                .orElseThrow(() ->
                        new AttendanceException(
                                "Employee has not checked in today"));

        if (attendance.getCheckOut() != null) {

            throw new AttendanceException(
                    "Employee has already checked out today");
        }

        LocalDateTime checkOut = LocalDateTime.now();

        attendance.setCheckOut(checkOut);

        Duration duration = Duration.between(
                attendance.getCheckIn(),
                checkOut
        );

        double workingHours =
                Math.round((duration.toMinutes() / 60.0) * 100.0) / 100.0;

        attendance.setWorkingHours(workingHours);

        Attendance updatedAttendance =
                attendanceRepository.save(attendance);

        return convertToDTO(updatedAttendance);
    }

    public List<AttendanceDTO> getAttendanceByEmployee(Long employeeId) {

        validateEmployeeAccess(employeeId);

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Employee not found with id: " + employeeId));

        return attendanceRepository.findByEmployee(employee)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<AttendanceDTO> getAttendanceByDate(LocalDate date) {

        return attendanceRepository.findByAttendanceDate(date)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private AttendanceDTO convertToDTO(Attendance attendance) {

        Employee employee = attendance.getEmployee();

        return new AttendanceDTO(
                attendance.getId(),
                employee.getId(),
                employee.getName(),
                attendance.getAttendanceDate(),
                attendance.getCheckIn(),
                attendance.getCheckOut(),
                attendance.getWorkingHours()
        );
    }
}