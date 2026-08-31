package com.example.work_management_system.service.impl;

import com.example.work_management_system.dto.UserRequest;
import com.example.work_management_system.dto.UserResponse;
import com.example.work_management_system.entity.User;
import com.example.work_management_system.exception.UserNotFoundException;
import com.example.work_management_system.repository.UserRepository;
import com.example.work_management_system.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserResponse createUser(UserRequest request) {

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .role(request.getRole())
                .active(true)
                .build();

        User savedUser = userRepository.save(user);

        return UserResponse.builder()
                .id(savedUser.getId())
                .name(savedUser.getName())
                .email(savedUser.getEmail())
                .role(savedUser.getRole())
                .active(savedUser.isActive())
                .createdAt(savedUser.getCreatedAt())
                .updatedAt(savedUser.getUpdatedAt())
                .build();
    }

    @Override
    public List<UserResponse> getAllUsers() {

        return userRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public UserResponse getUserById(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        return mapToResponse(user);
    }

    @Override
    public UserResponse updateUser(Long id, UserRequest request) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setRole(request.getRole());

        User updatedUser = userRepository.save(user);

        return mapToResponse(updatedUser);
    }

    @Override
    public void deactivateUser(Long id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException(id));

        user.setActive(false);

        userRepository.save(user);
    }

    private UserResponse mapToResponse(User user) {

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .active(user.isActive())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}