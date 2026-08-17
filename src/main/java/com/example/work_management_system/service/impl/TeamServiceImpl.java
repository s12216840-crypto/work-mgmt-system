package com.example.work_management_system.service.impl;

import com.example.work_management_system.dto.TeamRequest;
import com.example.work_management_system.dto.TeamResponse;
import com.example.work_management_system.entity.Organization;
import com.example.work_management_system.entity.Team;
import com.example.work_management_system.entity.User;
import com.example.work_management_system.exception.OrganizationNotFoundException;
import com.example.work_management_system.exception.TeamNotFoundException;
import com.example.work_management_system.exception.UserNotFoundException;
import com.example.work_management_system.repository.OrganizationRepository;
import com.example.work_management_system.repository.TeamRepository;
import com.example.work_management_system.repository.UserRepository;
import com.example.work_management_system.service.TeamService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TeamServiceImpl implements TeamService {

    private final TeamRepository teamRepository;
    private final OrganizationRepository organizationRepository;
    private final UserRepository userRepository;

    @Override
    public TeamResponse createTeam(TeamRequest request) {

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                "Organization not found with id: "
                                        + request.getOrganizationId()));

        Team team = Team.builder()
                .name(request.getName())
                .description(request.getDescription())
                .organization(organization)
                .build();

        Team savedTeam = teamRepository.save(team);

        return mapToResponse(savedTeam);
    }

    @Override
    public List<TeamResponse> getAllTeams() {

        return teamRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public TeamResponse getTeamById(Long id) {

        Team team = teamRepository.findById(id)
                .orElseThrow(() ->
                        new TeamNotFoundException(
                                "Team not found with id: " + id));

        return mapToResponse(team);
    }

    @Override
    public TeamResponse updateTeam(Long id, TeamRequest request) {

        Team team = teamRepository.findById(id)
                .orElseThrow(() ->
                        new TeamNotFoundException(
                                "Team not found with id: " + id));

        Organization organization = organizationRepository
                .findById(request.getOrganizationId())
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                "Organization not found with id: "
                                        + request.getOrganizationId()));

        team.setName(request.getName());
        team.setDescription(request.getDescription());
        team.setOrganization(organization);

        Team updatedTeam = teamRepository.save(team);

        return mapToResponse(updatedTeam);
    }

    @Override
    public void deleteTeam(Long id) {

        Team team = teamRepository.findById(id)
                .orElseThrow(() ->
                        new TeamNotFoundException(
                                "Team not found with id: " + id));

        teamRepository.delete(team);
    }

    @Override
    public void addUserToTeam(Long teamId, Long userId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new TeamNotFoundException(
                                "Team not found with id: " + teamId));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                  userId));

        if (!team.getUsers().contains(user)) {
            team.getUsers().add(user);
            teamRepository.save(team);
        }
    }

    @Override
    public void removeUserFromTeam(Long teamId, Long userId) {

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() ->
                        new TeamNotFoundException(
                                "Team not found with id: " + teamId));

        User user = userRepository.findById(userId)
                .orElseThrow(() ->
                        new UserNotFoundException(
                                 userId));

        team.getUsers().remove(user);

        teamRepository.save(team);
    }

    private TeamResponse mapToResponse(Team team) {

        List<Long> userIds = team.getUsers()
                .stream()
                .map(User::getId)
                .toList();

        return TeamResponse.builder()
                .id(team.getId())
                .name(team.getName())
                .description(team.getDescription())
                .organizationId(team.getOrganization().getId())
                .userIds(userIds)
                .build();
    }
}