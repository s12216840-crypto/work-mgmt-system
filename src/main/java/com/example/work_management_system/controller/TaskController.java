package com.example.work_management_system.controller;

import com.example.work_management_system.dto.TaskRequest;
import com.example.work_management_system.dto.TaskResponse;
import com.example.work_management_system.service.TaskService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")@RequiredArgsConstructor
public class TaskController {

    private final TaskService taskService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse createTask(
            @Valid @RequestBody TaskRequest request) {

        return taskService.createTask(request);
    }

    @GetMapping
    public List<TaskResponse> getAllTasks() {

        return taskService.getAllTasks();
    }

    @GetMapping("/{id}")
    public TaskResponse getTaskById(
            @PathVariable Long id) {

        return taskService.getTaskById(id);
    }

    @PutMapping("/{id}")
    public TaskResponse updateTask(
            @PathVariable Long id,
            @Valid @RequestBody TaskRequest request) {

        return taskService.updateTask(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTask(
            @PathVariable Long id) {

        taskService.deleteTask(id);
    }

    @PatchMapping("/{taskId}/assignee/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignTask(
            @PathVariable Long taskId,
            @PathVariable Long userId) {

        taskService.assignTask(taskId, userId);
    }

    @PatchMapping("/{taskId}/status/{status}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changeStatus(
            @PathVariable Long taskId,
            @PathVariable String status) {

        taskService.changeStatus(taskId, status);
    }

    @PatchMapping("/{taskId}/priority/{priority}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePriority(
            @PathVariable Long taskId,
            @PathVariable String priority) {

        taskService.changePriority(taskId, priority);
    }
}