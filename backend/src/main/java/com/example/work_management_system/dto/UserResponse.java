package com.example.work_management_system.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class UserResponse {

    private Long id;

    private String name;

    private String email;

    private String role;

    private boolean active;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}