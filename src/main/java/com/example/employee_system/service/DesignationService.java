package com.example.employee_system.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employee_system.entity.Designation;
import com.example.employee_system.repository.DesignationRepository;

@Service
public class DesignationService {

    private final DesignationRepository designationRepository;

    public DesignationService(DesignationRepository designationRepository) {
        this.designationRepository = designationRepository;
    }

    public Designation addDesignation(Designation designation) {
        return designationRepository.save(designation);
    }

    public List<Designation> getAllDesignations() {
        return designationRepository.findAll();
    }

    public Designation updateDesignation(Long id, Designation designation) {

    Designation existingDesignation = designationRepository.findById(id)
            .orElseThrow(() -> new RuntimeException(
                    "Designation not found with id: " + id));

    existingDesignation.setName(designation.getName());

    return designationRepository.save(existingDesignation);
}

public void deleteDesignation(Long id) {

    if (!designationRepository.existsById(id)) {
        throw new RuntimeException(
                "Designation not found with id: " + id);
    }

    designationRepository.deleteById(id);
}
}