package com.example.work_management_system.service.impl;

import com.example.work_management_system.dto.UserRequest;
import com.example.work_management_system.dto.UserResponse;
import com.example.work_management_system.entity.Role;
import com.example.work_management_system.entity.User;
import com.example.work_management_system.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;


    @Test
    void createUser_shouldCreateUserSuccessfully() {

        UserRequest request = new UserRequest();

        request.setName("Nizar");
        request.setEmail("nizar@gmail.com");
        request.setPassword("123456");
        request.setRole(Role.DEVELOPER);

        when(passwordEncoder.encode("123456"))
                .thenReturn("encodedPassword");

        User savedUser = User.builder()
                .id(1L)
                .name("Nizar")
                .email("nizar@gmail.com")
                .password("encodedPassword")
                .role(Role.DEVELOPER)
                .active(true)
                .build();

        when(userRepository.save(any(User.class)))
                .thenReturn(savedUser);

        UserResponse response =
                userService.createUser(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Nizar", response.getName());
        assertEquals("nizar@gmail.com", response.getEmail());
        assertEquals(Role.DEVELOPER, response.getRole());
        assertTrue(response.isActive());

        verify(passwordEncoder)
                .encode("123456");

        verify(userRepository)
                .save(any(User.class));
    }


    @Test
    void getAllUsers_shouldReturnUsers() {

        User user1 = User.builder()
                .id(1L)
                .name("Nizar")
                .email("nizar@gmail.com")
                .role(Role.DEVELOPER)
                .active(true)
                .build();

        User user2 = User.builder()
                .id(2L)
                .name("Ahmad")
                .email("ahmad@gmail.com")
                .role(Role.MANAGER)
                .active(true)
                .build();

        when(userRepository.findAll())
                .thenReturn(List.of(user1, user2));

        List<UserResponse> result =
                userService.getAllUsers();

        assertEquals(2, result.size());

        assertEquals(
                "Nizar",
                result.get(0).getName()
        );

        assertEquals(
                "Ahmad",
                result.get(1).getName()
        );

        assertEquals(
                Role.DEVELOPER,
                result.get(0).getRole()
        );

        assertEquals(
                Role.MANAGER,
                result.get(1).getRole()
        );

        verify(userRepository)
                .findAll();
    }


    @Test
    void getUserById_shouldReturnUser() {

        User user = User.builder()
                .id(1L)
                .name("Nizar")
                .email("nizar@gmail.com")
                .role(Role.DEVELOPER)
                .active(true)
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        UserResponse response =
                userService.getUserById(1L);

        assertNotNull(response);

        assertEquals(
                1L,
                response.getId()
        );

        assertEquals(
                "Nizar",
                response.getName()
        );

        assertEquals(
                "nizar@gmail.com",
                response.getEmail()
        );

        assertEquals(
                Role.DEVELOPER,
                response.getRole()
        );

        verify(userRepository)
                .findById(1L);
    }


    @Test
    void updateUser_shouldUpdateUser() {

        User user = User.builder()
                .id(1L)
                .name("Old Name")
                .email("old@gmail.com")
                .password("oldPassword")
                .role(Role.DEVELOPER)
                .active(true)
                .build();

        UserRequest request = new UserRequest();

        request.setName("New Name");
        request.setEmail("new@gmail.com");
        request.setPassword("654321");
        request.setRole(Role.MANAGER);

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        when(passwordEncoder.encode("654321"))
                .thenReturn("newEncodedPassword");

        when(userRepository.save(any(User.class)))
                .thenReturn(user);

        UserResponse response =
                userService.updateUser(1L, request);

        assertEquals(
                "New Name",
                response.getName()
        );

        assertEquals(
                "new@gmail.com",
                response.getEmail()
        );

        assertEquals(
                Role.MANAGER,
                response.getRole()
        );

        verify(userRepository)
                .findById(1L);

        verify(passwordEncoder)
                .encode("654321");

        verify(userRepository)
                .save(user);
    }


    @Test
    void deactivateUser_shouldDeactivateUser() {

        User user = User.builder()
                .id(1L)
                .name("Nizar")
                .email("nizar@gmail.com")
                .role(Role.DEVELOPER)
                .active(true)
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        userService.deactivateUser(1L);

        assertFalse(user.isActive());

        verify(userRepository)
                .findById(1L);

        verify(userRepository)
                .save(user);
    }


    @Test
    void getUserById_shouldThrowExceptionWhenUserNotFound() {

        when(userRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> userService.getUserById(999L)
        );

        verify(userRepository)
                .findById(999L);
    }
}