package com.example.work_management_system.controller;

import com.example.work_management_system.dto.OrganizationRequest;
import com.example.work_management_system.dto.OrganizationResponse;
import com.example.work_management_system.service.OrganizationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(ApiPaths.ORGANIZATIONS)
@RequiredArgsConstructor
public class OrganizationController {

    private final OrganizationService organizationService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OrganizationResponse createOrganization(
            @Valid @RequestBody OrganizationRequest request) {

        return organizationService.createOrganization(request);
    }

    @GetMapping
    public List<OrganizationResponse> getAllOrganizations() {

        return organizationService.getAllOrganizations();
    }

    @GetMapping("/{id}")
    public OrganizationResponse getOrganizationById(
            @PathVariable Long id) {

        return organizationService.getOrganizationById(id);
    }

    @PutMapping("/{id}")
    public OrganizationResponse updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationRequest request) {

        return organizationService.updateOrganization(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrganization(
            @PathVariable Long id) {

        organizationService.deleteOrganization(id);
    }
}