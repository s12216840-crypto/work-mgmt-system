package com.example.work_management_system.exception;

public class OrganizationNotFoundException extends RuntimeException {

    public OrganizationNotFoundException(String message) {
        super(message);
    }
}