package com.example.employee_system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employee_system.entity.Department;
import com.example.employee_system.repository.DepartmentRepository;

@Service
public class DepartmentService {

    private final DepartmentRepository departmentRepository;

    public DepartmentService(DepartmentRepository departmentRepository) {
        this.departmentRepository = departmentRepository;
    }

    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }

    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    public Department updateDepartment(Long id, Department department) {

    Department existingDepartment = departmentRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Department not found with id: " + id));

    existingDepartment.setName(department.getName());

    return departmentRepository.save(existingDepartment);
}

public void deleteDepartment(Long id) {

    if (!departmentRepository.existsById(id)) {
        throw new RuntimeException("Department not found with id: " + id);
    }

    departmentRepository.deleteById(id);
}
}