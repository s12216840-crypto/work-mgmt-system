package com.example.work_management_system.service.impl;

import com.example.work_management_system.dto.TaskRequest;
import com.example.work_management_system.dto.TaskResponse;
import com.example.work_management_system.entity.Project;
import com.example.work_management_system.entity.Task;
import com.example.work_management_system.entity.TaskPriority;
import com.example.work_management_system.entity.TaskStatus;
import com.example.work_management_system.entity.User;
import com.example.work_management_system.exception.TaskNotFoundException;
import com.example.work_management_system.repository.ProjectRepository;
import com.example.work_management_system.repository.TaskRepository;
import com.example.work_management_system.repository.UserRepository;
import com.example.work_management_system.service.TaskService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    @Override
    public TaskResponse createTask(TaskRequest request) {

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        User assignee = null;

        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        Task task = Task.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .project(project)
                .assignee(assignee)
                .reporter(reporter)
                .status(request.getStatus())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .build();

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public List<TaskResponse> getAllTasks() {
        return taskRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TaskResponse getTaskById(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + id));

        return mapToResponse(task);
    }

    @Override
    public TaskResponse updateTask(Long id, TaskRequest request) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + id));

        Project project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new RuntimeException("Project not found"));

        User reporter = userRepository.findById(request.getReporterId())
                .orElseThrow(() -> new RuntimeException("Reporter not found"));

        User assignee = null;

        if (request.getAssigneeId() != null) {
            assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new RuntimeException("Assignee not found"));
        }

        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setProject(project);
        task.setReporter(reporter);
        task.setAssignee(assignee);
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());

        return mapToResponse(taskRepository.save(task));
    }

    @Override
    public void deleteTask(Long id) {

        Task task = taskRepository.findById(id)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + id));

        taskRepository.delete(task);
    }

    @Override
    public void assignTask(Long taskId, Long userId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + taskId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        task.setAssignee(user);
        taskRepository.save(task);
    }

    @Override
    public void changeStatus(Long taskId, String status) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + taskId));

        try {
            task.setStatus(TaskStatus.valueOf(status.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid task status: " + status);
        }

        taskRepository.save(task);
    }

    @Override
    public void changePriority(Long taskId, String priority) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + taskId));

        try {
            task.setPriority(TaskPriority.valueOf(priority.toUpperCase()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Invalid task priority: " + priority);
        }

        taskRepository.save(task);
    }

    @Override
    public Page<TaskResponse> searchTasks(
            TaskStatus status,
            TaskPriority priority,
            Long assigneeId,
            Long projectId,
            String search,
            int page,
            int size,
            String sortBy,
            String direction) {

        Sort.Direction sortDirection;

        try {
            sortDirection = Sort.Direction.fromString(direction);
        } catch (IllegalArgumentException e) {
            sortDirection = Sort.Direction.ASC;
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                Sort.by(sortDirection, sortBy)
        );

        return taskRepository.searchTasks(
                status,
                priority,
                assigneeId,
                projectId,
                search,
                pageable
        ).map(this::mapToResponse);
    }

    private TaskResponse mapToResponse(Task task) {

        return TaskResponse.builder()
                .id(task.getId())
                .title(task.getTitle())
                .description(task.getDescription())
                .projectId(task.getProject().getId())
                .assigneeId(
                        task.getAssignee() != null
                                ? task.getAssignee().getId()
                                : null)
                .reporterId(task.getReporter().getId())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .createdAt(task.getCreatedAt())
                .updatedAt(task.getUpdatedAt())
                .build();
    }
}