package com.example.work_management_system.service;

import com.example.work_management_system.dto.UserRequest;
import com.example.work_management_system.dto.UserResponse;

import java.util.List;

public interface UserService {

    UserResponse createUser(UserRequest request);

    List<UserResponse> getAllUsers();

    UserResponse getUserById(Long id);

    UserResponse updateUser(Long id, UserRequest request);

    void deactivateUser(Long id);
}