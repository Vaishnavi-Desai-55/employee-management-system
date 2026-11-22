package com.example.employee_system.dto;

public class LoginResponse {

    private String message;
    private String token;
    private Long employeeId;
    private String name;
    private String role;

    public LoginResponse() {
    }

    public LoginResponse(String message, String token, Long employeeId,
                         String name, String role) {
        this.message = message;
        this.token = token;
        this.employeeId = employeeId;
        this.name = name;
        this.role = role;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}