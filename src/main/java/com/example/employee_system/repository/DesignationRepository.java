package com.example.employee_system.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.employee_system.entity.Designation;

public interface DesignationRepository extends JpaRepository<Designation, Long> {

}