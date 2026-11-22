package com.example.employee_system.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class AttendanceDTO {

    private Long id;
    private Long employeeId;
    private String employeeName;
    private LocalDate attendanceDate;
    private LocalDateTime checkIn;
    private LocalDateTime checkOut;
    private Double workingHours;

    public AttendanceDTO() {
    }

    public AttendanceDTO(
            Long id,
            Long employeeId,
            String employeeName,
            LocalDate attendanceDate,
            LocalDateTime checkIn,
            LocalDateTime checkOut,
            Double workingHours) {

        this.id = id;
        this.employeeId = employeeId;
        this.employeeName = employeeName;
        this.attendanceDate = attendanceDate;
        this.checkIn = checkIn;
        this.checkOut = checkOut;
        this.workingHours = workingHours;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public LocalDate getAttendanceDate() {
        return attendanceDate;
    }

    public void setAttendanceDate(LocalDate attendanceDate) {
        this.attendanceDate = attendanceDate;
    }

    public LocalDateTime getCheckIn() {
        return checkIn;
    }

    public void setCheckIn(LocalDateTime checkIn) {
        this.checkIn = checkIn;
    }

    public LocalDateTime getCheckOut() {
        return checkOut;
    }

    public void setCheckOut(LocalDateTime checkOut) {
        this.checkOut = checkOut;
    }

    public Double getWorkingHours() {
        return workingHours;
    }

    public void setWorkingHours(Double workingHours) {
        this.workingHours = workingHours;
    }
}