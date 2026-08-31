package com.example.work_management_system.exception;

import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationErrors(
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

    @ExceptionHandler(UserNotFoundException.class)
    public Map<String, String> handleUserNotFound(
            UserNotFoundException exception) {

        Map<String, String> error = new HashMap<>();

        error.put("error", exception.getMessage());

        return error;
    }

    @ExceptionHandler(OrganizationNotFoundException.class)
    public Map<String, String> handleOrganizationNotFound(
            OrganizationNotFoundException exception) {

        Map<String, String> error = new HashMap<>();

        error.put("error", exception.getMessage());

        return error;
    }

    @ExceptionHandler(TeamNotFoundException.class)
    public Map<String, String> handleTeamNotFound(
            TeamNotFoundException exception) {

        Map<String, String> error = new HashMap<>();

        error.put("error", exception.getMessage());

        return error;
    }
    @ExceptionHandler(TaskNotFoundException.class)
    public Map<String, String> handleTaskNotFound(
            TaskNotFoundException exception) {

        Map<String, String> error = new HashMap<>();
        error.put("error", exception.getMessage());

        return error;
    }
}