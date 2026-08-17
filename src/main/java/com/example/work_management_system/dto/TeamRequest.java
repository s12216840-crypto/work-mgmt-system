package com.example.work_management_system.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TeamRequest {

    @NotBlank(message = "Team name is required")
    private String name;

    private String description;

    @NotNull(message = "Organization ID is required")
    private Long organizationId;
}