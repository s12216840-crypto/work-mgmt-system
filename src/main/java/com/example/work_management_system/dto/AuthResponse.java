package com.example.work_management_system.dto;

import com.example.work_management_system.entity.Role;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthResponse {

    private String token;

    private String tokenType;

    private Long userId;

    private String name;

    private String email;

    private Role role;
}