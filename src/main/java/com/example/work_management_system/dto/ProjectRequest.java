package com.example.work_management_system.dto;

import com.example.work_management_system.entity.ProjectStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ProjectRequest {

    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long organizationId;

    @NotNull
    private Long ownerId;

    @NotNull
    private ProjectStatus status;

    private LocalDate startDate;

    private LocalDate endDate;
}