package com.example.work_management_system.service;

import com.example.work_management_system.dto.ProjectRequest;
import com.example.work_management_system.dto.ProjectResponse;

import java.util.List;

public interface ProjectService {

    ProjectResponse createProject(ProjectRequest request);

    List<ProjectResponse> getAllProjects();

    ProjectResponse getProjectById(Long id);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void deleteProject(Long id);

    void addMember(Long projectId, Long userId);

    void removeMember(Long projectId, Long userId);
}