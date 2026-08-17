package com.example.work_management_system.service;

import com.example.work_management_system.dto.OrganizationRequest;
import com.example.work_management_system.dto.OrganizationResponse;

import java.util.List;

public interface OrganizationService {

    OrganizationResponse createOrganization(OrganizationRequest request);

    List<OrganizationResponse> getAllOrganizations();

    OrganizationResponse getOrganizationById(Long id);

    OrganizationResponse updateOrganization(Long id, OrganizationRequest request);

    void deleteOrganization(Long id);
}