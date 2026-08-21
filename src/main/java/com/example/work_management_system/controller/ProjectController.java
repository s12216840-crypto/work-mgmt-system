package com.example.work_management_system.controller;

import com.example.work_management_system.dto.ProjectRequest;
import com.example.work_management_system.dto.ProjectResponse;
import com.example.work_management_system.service.ProjectService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectService projectService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse createProject(
            @Valid @RequestBody ProjectRequest request) {

        return projectService.createProject(request);
    }

    @GetMapping
    public List<ProjectResponse> getAllProjects() {

        return projectService.getAllProjects();
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(
            @PathVariable Long id) {

        return projectService.getProjectById(id);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(
            @PathVariable Long id,
            @Valid @RequestBody ProjectRequest request) {

        return projectService.updateProject(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProject(
            @PathVariable Long id) {

        projectService.deleteProject(id);
    }

    @PostMapping("/{projectId}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        projectService.addMember(projectId, userId);
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeMember(
            @PathVariable Long projectId,
            @PathVariable Long userId) {

        projectService.removeMember(projectId, userId);
    }
}