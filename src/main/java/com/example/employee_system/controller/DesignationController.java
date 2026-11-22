package com.example.employee_system.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee_system.entity.Designation;
import com.example.employee_system.service.DesignationService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/designations")
@SecurityRequirement(name = "bearerAuth")
public class DesignationController {

    private final DesignationService designationService;

    public DesignationController(DesignationService designationService) {
        this.designationService = designationService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Designation addDesignation(@RequestBody Designation designation) {
        return designationService.addDesignation(designation);
    }

    @GetMapping
    public List<Designation> getAllDesignations() {
        return designationService.getAllDesignations();
    }

    @PutMapping("/{id}")
    public Designation updateDesignation(
            @PathVariable Long id,
            @RequestBody Designation designation) {

        return designationService.updateDesignation(id, designation);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteDesignation(@PathVariable Long id) {
        designationService.deleteDesignation(id);
    }
}