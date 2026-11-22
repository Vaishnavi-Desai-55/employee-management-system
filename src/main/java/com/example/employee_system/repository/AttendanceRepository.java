package com.example.employee_system.repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee_system.entity.Attendance;
import com.example.employee_system.entity.Employee;

public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeAndAttendanceDate(
            Employee employee,
            LocalDate attendanceDate
    );

    List<Attendance> findByEmployee(Employee employee);

    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);
}