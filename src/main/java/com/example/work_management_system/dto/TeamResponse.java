package com.example.work_management_system.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class TeamResponse {

    private Long id;
    private String name;
    private String description;
    private Long organizationId;
    private List<Long> userIds;
}