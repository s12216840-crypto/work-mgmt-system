package com.example.work_management_system.service.impl;

import com.example.work_management_system.dto.OrganizationRequest;
import com.example.work_management_system.dto.OrganizationResponse;
import com.example.work_management_system.entity.Organization;
import com.example.work_management_system.exception.OrganizationNotFoundException;
import com.example.work_management_system.repository.OrganizationRepository;
import com.example.work_management_system.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrganizationServiceImpl implements OrganizationService {

    private final OrganizationRepository organizationRepository;

    @Override
    public OrganizationResponse createOrganization(OrganizationRequest request) {

        Organization organization = Organization.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        Organization savedOrganization = organizationRepository.save(organization);

        return mapToResponse(savedOrganization);
    }

    @Override
    public List<OrganizationResponse> getAllOrganizations() {

        return organizationRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public OrganizationResponse getOrganizationById(Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                "Organization not found with id: " + id));

        return mapToResponse(organization);
    }

    @Override
    public OrganizationResponse updateOrganization(
            Long id,
            OrganizationRequest request) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                "Organization not found with id: " + id));

        organization.setName(request.getName());
        organization.setDescription(request.getDescription());

        Organization updatedOrganization =
                organizationRepository.save(organization);

        return mapToResponse(updatedOrganization);
    }

    @Override
    public void deleteOrganization(Long id) {

        Organization organization = organizationRepository.findById(id)
                .orElseThrow(() ->
                        new OrganizationNotFoundException(
                                "Organization not found with id: " + id));

        organizationRepository.delete(organization);
    }

    private OrganizationResponse mapToResponse(Organization organization) {

        return OrganizationResponse.builder()
                .id(organization.getId())
                .name(organization.getName())
                .description(organization.getDescription())
                .build();
    }
}