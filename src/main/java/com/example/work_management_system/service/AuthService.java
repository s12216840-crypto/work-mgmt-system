package com.example.work_management_system.service;

import com.example.work_management_system.dto.AuthResponse;
import com.example.work_management_system.dto.LoginRequest;
import com.example.work_management_system.dto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}