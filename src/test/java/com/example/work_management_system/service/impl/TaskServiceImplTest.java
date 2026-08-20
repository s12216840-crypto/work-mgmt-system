package com.example.work_management_system.service.impl;

import com.example.work_management_system.dto.TaskRequest;
import com.example.work_management_system.dto.TaskResponse;
import com.example.work_management_system.entity.Project;
import com.example.work_management_system.entity.Task;
import com.example.work_management_system.entity.TaskPriority;
import com.example.work_management_system.entity.TaskStatus;
import com.example.work_management_system.entity.User;
import com.example.work_management_system.repository.ProjectRepository;
import com.example.work_management_system.repository.TaskRepository;
import com.example.work_management_system.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TaskServiceImplTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskServiceImpl taskService;


    @Test
    void createTask_shouldCreateTaskSuccessfully() {

        TaskRequest request = new TaskRequest();

        request.setTitle("Implement Task Management");
        request.setDescription("Create task management module");
        request.setProjectId(1L);
        request.setAssigneeId(7L);
        request.setReporterId(8L);
        request.setStatus(TaskStatus.TODO);
        request.setPriority(TaskPriority.HIGH);
        request.setDueDate(LocalDate.of(2026, 9, 15));

        Project project = Project.builder()
                .id(1L)
                .name("Work Management System")
                .build();

        User assignee = User.builder()
                .id(7L)
                .name("Hasan Ali")
                .email("hasan.ali@gmail.com")
                .build();

        User reporter = User.builder()
                .id(8L)
                .name("Nizar Kassab")
                .email("nizar.kassab@gmail.com")
                .build();

        Task savedTask = Task.builder()
                .id(1L)
                .title("Implement Task Management")
                .description("Create task management module")
                .project(project)
                .assignee(assignee)
                .reporter(reporter)
                .status(TaskStatus.TODO)
                .priority(TaskPriority.HIGH)
                .dueDate(request.getDueDate())
                .build();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(7L))
                .thenReturn(Optional.of(assignee));

        when(userRepository.findById(8L))
                .thenReturn(Optional.of(reporter));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        TaskResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Implement Task Management", response.getTitle());
        assertEquals("Create task management module", response.getDescription());
        assertEquals(1L, response.getProjectId());
        assertEquals(7L, response.getAssigneeId());
        assertEquals(8L, response.getReporterId());
        assertEquals(TaskStatus.TODO, response.getStatus());
        assertEquals(TaskPriority.HIGH, response.getPriority());
        assertEquals(LocalDate.of(2026, 9, 15), response.getDueDate());

        verify(projectRepository).findById(1L);
        verify(userRepository).findById(7L);
        verify(userRepository).findById(8L);
        verify(taskRepository).save(any(Task.class));
    }


    @Test
    void createTask_shouldCreateTaskWithoutAssignee() {

        TaskRequest request = new TaskRequest();

        request.setTitle("Unassigned Task");
        request.setDescription("Task without assignee");
        request.setProjectId(1L);
        request.setAssigneeId(null);
        request.setReporterId(8L);
        request.setStatus(TaskStatus.TODO);
        request.setPriority(TaskPriority.MEDIUM);

        Project project = Project.builder()
                .id(1L)
                .name("Work Management System")
                .build();

        User reporter = User.builder()
                .id(8L)
                .name("Nizar Kassab")
                .build();

        Task savedTask = Task.builder()
                .id(2L)
                .title("Unassigned Task")
                .description("Task without assignee")
                .project(project)
                .reporter(reporter)
                .status(TaskStatus.TODO)
                .priority(TaskPriority.MEDIUM)
                .build();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(8L))
                .thenReturn(Optional.of(reporter));

        when(taskRepository.save(any(Task.class)))
                .thenReturn(savedTask);

        TaskResponse response = taskService.createTask(request);

        assertNotNull(response);
        assertEquals(2L, response.getId());
        assertNull(response.getAssigneeId());
        assertEquals(TaskStatus.TODO, response.getStatus());
        assertEquals(TaskPriority.MEDIUM, response.getPriority());

        verify(projectRepository).findById(1L);
        verify(userRepository).findById(8L);
        verify(taskRepository).save(any(Task.class));

        verify(userRepository, never()).findById(null);
    }


    @Test
    void getAllTasks_shouldReturnTasks() {

        Project project = Project.builder()
                .id(1L)
                .name("Work Management System")
                .build();

        User reporter = User.builder()
                .id(8L)
                .name("Nizar Kassab")
                .build();

        Task task1 = Task.builder()
                .id(1L)
                .title("Task One")
                .description("First task")
                .project(project)
                .reporter(reporter)
                .status(TaskStatus.TODO)
                .priority(TaskPriority.LOW)
                .build();

        Task task2 = Task.builder()
                .id(2L)
                .title("Task Two")
                .description("Second task")
                .project(project)
                .reporter(reporter)
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .build();

        when(taskRepository.findAll())
                .thenReturn(List.of(task1, task2));

        List<TaskResponse> result = taskService.getAllTasks();

        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Task One", result.get(0).getTitle());
        assertEquals("Task Two", result.get(1).getTitle());

        assertEquals(TaskStatus.TODO, result.get(0).getStatus());
        assertEquals(TaskStatus.IN_PROGRESS, result.get(1).getStatus());

        verify(taskRepository).findAll();
    }


    @Test
    void getTaskById_shouldReturnTask() {

        Project project = Project.builder()
                .id(1L)
                .name("Work Management System")
                .build();

        User reporter = User.builder()
                .id(8L)
                .name("Nizar Kassab")
                .build();

        Task task = Task.builder()
                .id(1L)
                .title("Implement Backend")
                .description("Implement backend functionality")
                .project(project)
                .reporter(reporter)
                .status(TaskStatus.IN_PROGRESS)
                .priority(TaskPriority.HIGH)
                .build();

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        TaskResponse response = taskService.getTaskById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Implement Backend", response.getTitle());
        assertEquals(1L, response.getProjectId());
        assertEquals(8L, response.getReporterId());
        assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());
        assertEquals(TaskPriority.HIGH, response.getPriority());

        verify(taskRepository).findById(1L);
    }


    @Test
    void getTaskById_shouldThrowExceptionWhenNotFound() {

        when(taskRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> taskService.getTaskById(999L)
        );

        verify(taskRepository).findById(999L);
    }


    @Test
    void updateTask_shouldUpdateTaskSuccessfully() {

        Project oldProject = Project.builder()
                .id(1L)
                .name("Old Project")
                .build();

        Project newProject = Project.builder()
                .id(2L)
                .name("New Project")
                .build();

        User oldReporter = User.builder()
                .id(8L)
                .name("Nizar Kassab")
                .build();

        User newReporter = User.builder()
                .id(6L)
                .name("Omar Khalil")
                .build();

        User assignee = User.builder()
                .id(7L)
                .name("Hasan Ali")
                .build();

        Task task = Task.builder()
                .id(1L)
                .title("Old Task")
                .description("Old Description")
                .project(oldProject)
                .reporter(oldReporter)
                .status(TaskStatus.TODO)
                .priority(TaskPriority.LOW)
                .build();

        TaskRequest request = new TaskRequest();

        request.setTitle("Updated Task");
        request.setDescription("Updated Description");
        request.setProjectId(2L);
        request.setAssigneeId(7L);
        request.setReporterId(6L);
        request.setStatus(TaskStatus.IN_PROGRESS);
        request.setPriority(TaskPriority.CRITICAL);
        request.setDueDate(LocalDate.of(2026, 10, 1));

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(projectRepository.findById(2L))
                .thenReturn(Optional.of(newProject));

        when(userRepository.findById(6L))
                .thenReturn(Optional.of(newReporter));

        when(userRepository.findById(7L))
                .thenReturn(Optional.of(assignee));

        when(taskRepository.save(task))
                .thenReturn(task);

        TaskResponse response =
                taskService.updateTask(1L, request);

        assertNotNull(response);
        assertEquals("Updated Task", response.getTitle());
        assertEquals("Updated Description", response.getDescription());
        assertEquals(2L, response.getProjectId());
        assertEquals(7L, response.getAssigneeId());
        assertEquals(6L, response.getReporterId());
        assertEquals(TaskStatus.IN_PROGRESS, response.getStatus());
        assertEquals(TaskPriority.CRITICAL, response.getPriority());
        assertEquals(LocalDate.of(2026, 10, 1), response.getDueDate());

        verify(taskRepository).findById(1L);
        verify(projectRepository).findById(2L);
        verify(userRepository).findById(6L);
        verify(userRepository).findById(7L);
        verify(taskRepository).save(task);
    }


    @Test
    void deleteTask_shouldDeleteTaskSuccessfully() {

        Project project = Project.builder()
                .id(1L)
                .build();

        User reporter = User.builder()
                .id(8L)
                .build();

        Task task = Task.builder()
                .id(1L)
                .title("Task to Delete")
                .project(project)
                .reporter(reporter)
                .status(TaskStatus.TODO)
                .priority(TaskPriority.LOW)
                .build();

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        taskService.deleteTask(1L);

        verify(taskRepository).findById(1L);
        verify(taskRepository).delete(task);
    }


    @Test
    void assignTask_shouldAssignUserSuccessfully() {

        Project project = Project.builder()
                .id(1L)
                .build();

        User reporter = User.builder()
                .id(8L)
                .build();

        User assignee = User.builder()
                .id(7L)
                .name("Hasan Ali")
                .build();

        Task task = Task.builder()
                .id(1L)
                .title("Task")
                .project(project)
                .reporter(reporter)
                .status(TaskStatus.TODO)
                .priority(TaskPriority.HIGH)
                .build();

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        when(userRepository.findById(7L))
                .thenReturn(Optional.of(assignee));

        taskService.assignTask(1L, 7L);

        assertEquals(assignee, task.getAssignee());

        verify(taskRepository).findById(1L);
        verify(userRepository).findById(7L);
        verify(taskRepository).save(task);
    }


    @Test
    void changeStatus_shouldChangeStatusSuccessfully() {

        Project project = Project.builder()
                .id(1L)
                .build();

        User reporter = User.builder()
                .id(8L)
                .build();

        Task task = Task.builder()
                .id(1L)
                .title("Task")
                .project(project)
                .reporter(reporter)
                .status(TaskStatus.TODO)
                .priority(TaskPriority.HIGH)
                .build();

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        taskService.changeStatus(1L, "IN_PROGRESS");

        assertEquals(TaskStatus.IN_PROGRESS, task.getStatus());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
    }


    @Test
    void changePriority_shouldChangePrioritySuccessfully() {

        Project project = Project.builder()
                .id(1L)
                .build();

        User reporter = User.builder()
                .id(8L)
                .build();

        Task task = Task.builder()
                .id(1L)
                .title("Task")
                .project(project)
                .reporter(reporter)
                .status(TaskStatus.TODO)
                .priority(TaskPriority.LOW)
                .build();

        when(taskRepository.findById(1L))
                .thenReturn(Optional.of(task));

        taskService.changePriority(1L, "CRITICAL");

        assertEquals(TaskPriority.CRITICAL, task.getPriority());

        verify(taskRepository).findById(1L);
        verify(taskRepository).save(task);
    }
}