package com.example.work_management_system.exception;

public class TeamNotFoundException extends RuntimeException {

    public TeamNotFoundException(String message) {
        super(message);
    }
}