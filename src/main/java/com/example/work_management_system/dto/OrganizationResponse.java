package com.example.work_management_system.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrganizationResponse {

    private Long id;
    private String name;
    private String description;
}