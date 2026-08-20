package com.example.work_management_system.service;

import com.example.work_management_system.dto.TaskRequest;
import com.example.work_management_system.dto.TaskResponse;
import com.example.work_management_system.entity.TaskPriority;
import com.example.work_management_system.entity.TaskStatus;
import org.springframework.data.domain.Page;

import java.util.List;

public interface TaskService {

    TaskResponse createTask(TaskRequest request);

    List<TaskResponse> getAllTasks();

    TaskResponse getTaskById(Long id);

    TaskResponse updateTask(Long id, TaskRequest request);

    void deleteTask(Long id);

    void assignTask(Long taskId, Long userId);

    void changeStatus(Long taskId, String status);

    void changePriority(Long taskId, String priority);

    Page<TaskResponse> searchTasks(
            TaskStatus status,
            TaskPriority priority,
            Long assigneeId,
            Long projectId,
            String search,
            int page,
            int size,
            String sortBy,
            String direction
    );
}