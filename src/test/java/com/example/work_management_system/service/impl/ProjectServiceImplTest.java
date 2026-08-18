package com.example.work_management_system.service.impl;

import com.example.work_management_system.dto.ProjectRequest;
import com.example.work_management_system.dto.ProjectResponse;
import com.example.work_management_system.entity.Organization;
import com.example.work_management_system.entity.Project;
import com.example.work_management_system.entity.ProjectStatus;
import com.example.work_management_system.entity.User;
import com.example.work_management_system.repository.OrganizationRepository;
import com.example.work_management_system.repository.ProjectRepository;
import com.example.work_management_system.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceImplTest {

    @Mock
    private ProjectRepository projectRepository;

    @Mock
    private OrganizationRepository organizationRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProjectServiceImpl projectService;

    @Test
    void createProject_shouldCreateProjectSuccessfully() {

        ProjectRequest request = new ProjectRequest();
        request.setName("Work Management System");
        request.setDescription("Project management system");
        request.setOrganizationId(1L);
        request.setOwnerId(1L);
        request.setStatus(ProjectStatus.PLANNED);
        request.setStartDate(LocalDate.of(2026, 8, 18));
        request.setEndDate(LocalDate.of(2026, 12, 31));

        Organization organization = Organization.builder()
                .id(1L)
                .name("Tech Organization")
                .build();

        User owner = User.builder()
                .id(1L)
                .name("Nizar Kassab")
                .email("nizar@gmail.com")
                .build();

        Project savedProject = Project.builder()
                .id(1L)
                .name("Work Management System")
                .description("Project management system")
                .organization(organization)
                .owner(owner)
                .status(ProjectStatus.PLANNED)
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .members(new ArrayList<>())
                .build();

        when(organizationRepository.findById(1L))
                .thenReturn(Optional.of(organization));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(owner));

        when(projectRepository.save(any(Project.class)))
                .thenReturn(savedProject);

        ProjectResponse response =
                projectService.createProject(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Work Management System", response.getName());
        assertEquals("Project management system", response.getDescription());
        assertEquals(1L, response.getOrganizationId());
        assertEquals(1L, response.getOwnerId());
        assertEquals(ProjectStatus.PLANNED, response.getStatus());

        verify(organizationRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(projectRepository).save(any(Project.class));
    }

    @Test
    void getAllProjects_shouldReturnProjects() {

        Organization organization = Organization.builder()
                .id(1L)
                .name("Tech Organization")
                .build();

        User owner = User.builder()
                .id(1L)
                .name("Nizar Kassab")
                .build();

        Project project1 = Project.builder()
                .id(1L)
                .name("Work Management System")
                .description("Management project")
                .organization(organization)
                .owner(owner)
                .status(ProjectStatus.PLANNED)
                .members(new ArrayList<>())
                .build();

        Project project2 = Project.builder()
                .id(2L)
                .name("Mobile Application")
                .description("Mobile project")
                .organization(organization)
                .owner(owner)
                .status(ProjectStatus.IN_PROGRESS)
                .members(new ArrayList<>())
                .build();

        when(projectRepository.findAll())
                .thenReturn(List.of(project1, project2));

        List<ProjectResponse> result =
                projectService.getAllProjects();

        assertEquals(2, result.size());
        assertEquals("Work Management System", result.get(0).getName());
        assertEquals("Mobile Application", result.get(1).getName());

        verify(projectRepository).findAll();
    }

    @Test
    void getProjectById_shouldReturnProject() {

        Organization organization = Organization.builder()
                .id(1L)
                .build();

        User owner = User.builder()
                .id(1L)
                .name("Nizar Kassab")
                .build();

        Project project = Project.builder()
                .id(1L)
                .name("Work Management System")
                .description("Management project")
                .organization(organization)
                .owner(owner)
                .status(ProjectStatus.PLANNED)
                .members(new ArrayList<>())
                .build();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        ProjectResponse response =
                projectService.getProjectById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Work Management System", response.getName());

        verify(projectRepository).findById(1L);
    }

    @Test
    void getProjectById_shouldThrowExceptionWhenNotFound() {

        when(projectRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                RuntimeException.class,
                () -> projectService.getProjectById(999L)
        );

        verify(projectRepository).findById(999L);
    }

    @Test
    void updateProject_shouldUpdateProjectSuccessfully() {

        Organization organization = Organization.builder()
                .id(1L)
                .name("Tech Organization")
                .build();

        User owner = User.builder()
                .id(1L)
                .name("Nizar Kassab")
                .build();

        Project project = Project.builder()
                .id(1L)
                .name("Old Project")
                .description("Old Description")
                .organization(organization)
                .owner(owner)
                .status(ProjectStatus.PLANNED)
                .members(new ArrayList<>())
                .build();

        ProjectRequest request = new ProjectRequest();
        request.setName("Updated Project");
        request.setDescription("Updated Description");
        request.setOrganizationId(1L);
        request.setOwnerId(1L);
        request.setStatus(ProjectStatus.IN_PROGRESS);
        request.setStartDate(LocalDate.of(2026, 8, 18));
        request.setEndDate(LocalDate.of(2026, 12, 31));

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(organizationRepository.findById(1L))
                .thenReturn(Optional.of(organization));

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(owner));

        when(projectRepository.save(project))
                .thenReturn(project);

        ProjectResponse response =
                projectService.updateProject(1L, request);

        assertEquals("Updated Project", response.getName());
        assertEquals("Updated Description", response.getDescription());
        assertEquals(ProjectStatus.IN_PROGRESS, response.getStatus());

        verify(projectRepository).findById(1L);
        verify(organizationRepository).findById(1L);
        verify(userRepository).findById(1L);
        verify(projectRepository).save(project);
    }

    @Test
    void deleteProject_shouldDeleteProjectSuccessfully() {

        Organization organization = Organization.builder()
                .id(1L)
                .build();

        User owner = User.builder()
                .id(1L)
                .build();

        Project project = Project.builder()
                .id(1L)
                .name("Work Management System")
                .organization(organization)
                .owner(owner)
                .status(ProjectStatus.PLANNED)
                .members(new ArrayList<>())
                .build();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        projectService.deleteProject(1L);

        verify(projectRepository).findById(1L);
        verify(projectRepository).delete(project);
    }

    @Test
    void addMember_shouldAddUserToProject() {

        User member = User.builder()
                .id(2L)
                .name("Ahmad")
                .email("ahmad@gmail.com")
                .build();

        Project project = Project.builder()
                .id(1L)
                .name("Work Management System")
                .members(new ArrayList<>())
                .build();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        when(userRepository.findById(2L))
                .thenReturn(Optional.of(member));

        projectService.addMember(1L, 2L);

        assertEquals(1, project.getMembers().size());
        assertEquals(2L, project.getMembers().get(0).getId());

        verify(projectRepository).findById(1L);
        verify(userRepository).findById(2L);
        verify(projectRepository).save(project);
    }

    @Test
    void removeMember_shouldRemoveUserFromProject() {

        User member = User.builder()
                .id(2L)
                .name("Ahmad")
                .email("ahmad@gmail.com")
                .build();

        Project project = Project.builder()
                .id(1L)
                .name("Work Management System")
                .members(new ArrayList<>(List.of(member)))
                .build();

        when(projectRepository.findById(1L))
                .thenReturn(Optional.of(project));

        projectService.removeMember(1L, 2L);

        assertTrue(project.getMembers().isEmpty());

        verify(projectRepository).findById(1L);
        verify(projectRepository).save(project);
    }
}