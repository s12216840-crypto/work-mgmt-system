package com.example.work_management_system.service;

import com.example.work_management_system.dto.TeamRequest;
import com.example.work_management_system.dto.TeamResponse;

import java.util.List;

public interface TeamService {

    TeamResponse createTeam(TeamRequest request);

    List<TeamResponse> getAllTeams();

    TeamResponse getTeamById(Long id);

    TeamResponse updateTeam(Long id, TeamRequest request);

    void deleteTeam(Long id);

    void addUserToTeam(Long teamId, Long userId);

    void removeUserFromTeam(Long teamId, Long userId);
}