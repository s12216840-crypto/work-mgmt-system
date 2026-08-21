package com.example.work_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class LabelRequest {

    @NotBlank(message = "Label name is required")
    @Size(max = 50, message = "Label name cannot exceed 50 characters")
    private String name;
}