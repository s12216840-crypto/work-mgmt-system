package com.example.work_management_system.service.impl;
import com.example.work_management_system.dto.ProjectRequest;
import com.example.work_management_system.dto.ProjectResponse;
import com.example.work_management_system.entity.Organization;
import com.example.work_management_system.entity.Project;
import com.example.work_management_system.entity.User;
import com.example.work_management_system.exception.ProjectNotFoundException;
import com.example.work_management_system.repository.OrganizationRepository;
import com.example.work_management_system.repository.ProjectRepository;
import com.example.work_management_system.repository.UserRepository;
import com.example.work_management_system.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    @Override
    public ProjectResponse createProject(ProjectRequest request) {

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() ->
                        new RuntimeException("Organization not found"));

        User owner = userRepository
                .findById(request.getOwnerId())
                .orElseThrow(() ->
                        new RuntimeException("Owner not found"));

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .organization(organization)
                .owner(owner)
                .status(request.getStatus())
                .startDate(request.getStartDate())
                .endDate(request.getEndDate())
                .build();

        Project savedProject = projectRepository.save(project);

        return mapToResponse(savedProject);
    }

    @Override
    public List<ProjectResponse> getAllProjects() {

        return projectRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public ProjectResponse getProjectById(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with id: " + id));

        return mapToResponse(project);
    }

    @Override
    public ProjectResponse updateProject(
            Long id,
            ProjectRequest request) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with id: " + id));

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() ->
                        new RuntimeException("Organization not found"));

        User owner = userRepository
                .findById(request.getOwnerId())
                .orElseThrow(() ->
                        new RuntimeException("Owner not found"));

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOrganization(organization);
        project.setOwner(owner);
        project.setStatus(request.getStatus());
        project.setStartDate(request.getStartDate());
        project.setEndDate(request.getEndDate());

        Project updatedProject = projectRepository.save(project);

        return mapToResponse(updatedProject);
    }

    @Override
    public void deleteProject(Long id) {

        Project project = projectRepository.findById(id)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with id: " + id));

        projectRepository.delete(project);
    }

    @Override
    public void addMember(Long projectId, Long userId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with id: " + projectId));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        project.getMembers().add(user);

        projectRepository.save(project);
    }

    @Override
    public void removeMember(Long projectId, Long userId) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() ->
                        new ProjectNotFoundException(
                                "Project not found with id: " + projectId));

        project.getMembers()
                .removeIf(user -> user.getId().equals(userId));

        projectRepository.save(project);
    }

    private ProjectResponse mapToResponse(Project project) {

        return ProjectResponse.builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .organizationId(project.getOrganization().getId())
                .ownerId(project.getOwner().getId())
                .status(project.getStatus())
                .startDate(project.getStartDate())
                .endDate(project.getEndDate())
                .memberIds(
                        project.getMembers()
                                .stream()
                                .map(User::getId)
                                .toList()
                )
                .build();
    }
}