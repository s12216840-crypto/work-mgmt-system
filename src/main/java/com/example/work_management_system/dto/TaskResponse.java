package com.example.work_management_system.dto;

import com.example.work_management_system.entity.TaskPriority;
import com.example.work_management_system.entity.TaskStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
public class TaskResponse {

    private Long id;

    private String title;

    private String description;

    private Long projectId;

    private Long assigneeId;

    private Long reporterId;

    private TaskStatus status;

    private TaskPriority priority;

    private LocalDate dueDate;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}