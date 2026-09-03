package com.example.work_management_system.controller;

import com.example.work_management_system.dto.TeamRequest;
import com.example.work_management_system.dto.TeamResponse;
import com.example.work_management_system.service.TeamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
@RequiredArgsConstructor
public class TeamController {

    private final TeamService teamService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse createTeam(
            @Valid @RequestBody TeamRequest request) {

        return teamService.createTeam(request);
    }

    @GetMapping
    public List<TeamResponse> getAllTeams() {

        return teamService.getAllTeams();
    }

    @GetMapping("/{id}")
    public TeamResponse getTeamById(
            @PathVariable Long id) {

        return teamService.getTeamById(id);
    }

    @PutMapping("/{id}")
    public TeamResponse updateTeam(
            @PathVariable Long id,
            @Valid @RequestBody TeamRequest request) {

        return teamService.updateTeam(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTeam(
            @PathVariable Long id) {

        teamService.deleteTeam(id);
    }

    @PatchMapping("/{teamId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void addUserToTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId) {

        teamService.addUserToTeam(teamId, userId);
    }

    @DeleteMapping("/{teamId}/users/{userId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeUserFromTeam(
            @PathVariable Long teamId,
            @PathVariable Long userId) {

        teamService.removeUserFromTeam(teamId, userId);
    }
}