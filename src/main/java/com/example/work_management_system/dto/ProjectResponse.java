package com.example.work_management_system.dto;

import com.example.work_management_system.entity.ProjectStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class ProjectResponse {

    private Long id;

    private String name;

    private String description;

    private Long organizationId;

    private Long ownerId;

    private ProjectStatus status;

    private LocalDate startDate;

    private LocalDate endDate;

    private List<Long> memberIds;
}