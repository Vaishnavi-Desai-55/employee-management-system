package com.example.employee_system.exception;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleValidationExceptions(
            MethodArgumentNotValidException exception) {

        Map<String, String> errors = new HashMap<>();

        exception.getBindingResult()
                .getFieldErrors()
                .forEach(error ->
                        errors.put(
                                error.getField(),
                                error.getDefaultMessage()
                        )
                );

        return errors;
    }

    @ExceptionHandler(EmployeeNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Map<String, String> handleEmployeeNotFound(
            EmployeeNotFoundException exception) {

        Map<String, String> error = new HashMap<>();

        error.put("error", exception.getMessage());

        return error;
    }

    @ExceptionHandler(AttendanceException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public Map<String, String> handleAttendanceException(
            AttendanceException exception) {

        Map<String, String> error = new HashMap<>();

        error.put("error", exception.getMessage());

        return error;
    }
}