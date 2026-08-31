package com.example.work_management_system.dto;

import com.example.work_management_system.entity.TaskPriority;
import com.example.work_management_system.entity.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TaskRequest {

    @NotBlank(message = "Task title is required")
    private String title;

    private String description;

    @NotNull(message = "Project ID is required")
    private Long projectId;

    private Long assigneeId;

    @NotNull(message = "Reporter ID is required")
    private Long reporterId;

    @NotNull(message = "Task status is required")
    private TaskStatus status;

    @NotNull(message = "Task priority is required")
    private TaskPriority priority;

    private LocalDate dueDate;
}