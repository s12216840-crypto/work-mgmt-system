package com.example.work_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OrganizationRequest {

    @NotBlank(message = "Organization name is required")
    private String name;

    private String description;
}