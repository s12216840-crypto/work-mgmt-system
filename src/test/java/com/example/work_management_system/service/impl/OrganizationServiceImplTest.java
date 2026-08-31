package com.example.work_management_system.service.impl;

import com.example.work_management_system.dto.OrganizationRequest;
import com.example.work_management_system.dto.OrganizationResponse;
import com.example.work_management_system.entity.Organization;
import com.example.work_management_system.exception.OrganizationNotFoundException;
import com.example.work_management_system.repository.OrganizationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrganizationServiceImplTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationServiceImpl organizationService;

    @Test
    void createOrganization_shouldCreateOrganizationSuccessfully() {
        OrganizationRequest request = new OrganizationRequest();
        request.setName("Aseel Organization");
        request.setDescription("Training organization");

        Organization organization = Organization.builder()
                .id(1L)
                .name("Aseel Organization")
                .description("Training organization")
                .build();

        when(organizationRepository.save(any(Organization.class)))
                .thenReturn(organization);

        OrganizationResponse response =
                organizationService.createOrganization(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Aseel Organization", response.getName());
        assertEquals("Training organization", response.getDescription());

        verify(organizationRepository).save(any(Organization.class));
    }

    @Test
    void getAllOrganizations_shouldReturnAllOrganizations() {
        Organization organization1 = Organization.builder()
                .id(1L)
                .name("Organization One")
                .description("First organization")
                .build();

        Organization organization2 = Organization.builder()
                .id(2L)
                .name("Organization Two")
                .description("Second organization")
                .build();

        when(organizationRepository.findAll())
                .thenReturn(List.of(organization1, organization2));

        List<OrganizationResponse> responses =
                organizationService.getAllOrganizations();

        assertEquals(2, responses.size());
        assertEquals("Organization One", responses.get(0).getName());
        assertEquals("Organization Two", responses.get(1).getName());

        verify(organizationRepository).findAll();
    }

    @Test
    void getOrganizationById_shouldReturnOrganizationSuccessfully() {
        Organization organization = Organization.builder()
                .id(1L)
                .name("Aseel Organization")
                .description("Training organization")
                .build();

        when(organizationRepository.findById(1L))
                .thenReturn(Optional.of(organization));

        OrganizationResponse response =
                organizationService.getOrganizationById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Aseel Organization", response.getName());

        verify(organizationRepository).findById(1L);
    }

    @Test
    void getOrganizationById_shouldThrowExceptionWhenNotFound() {
        when(organizationRepository.findById(999L))
                .thenReturn(Optional.empty());

        assertThrows(
                OrganizationNotFoundException.class,
                () -> organizationService.getOrganizationById(999L)
        );

        verify(organizationRepository).findById(999L);
    }

    @Test
    void updateOrganization_shouldUpdateOrganizationSuccessfully() {
        Organization organization = Organization.builder()
                .id(1L)
                .name("Old Organization")
                .description("Old Description")
                .build();

        OrganizationRequest request = new OrganizationRequest();
        request.setName("Updated Organization");
        request.setDescription("Updated Description");

        when(organizationRepository.findById(1L))
                .thenReturn(Optional.of(organization));

        when(organizationRepository.save(organization))
                .thenReturn(organization);

        OrganizationResponse response =
                organizationService.updateOrganization(1L, request);

        assertEquals("Updated Organization", response.getName());
        assertEquals("Updated Description", response.getDescription());

        verify(organizationRepository).findById(1L);
        verify(organizationRepository).save(organization);
    }

    @Test
    void deleteOrganization_shouldDeleteOrganizationSuccessfully() {
        Organization organization = Organization.builder()
                .id(1L)
                .name("Aseel Organization")
                .description("Training organization")
                .build();

        when(organizationRepository.findById(1L))
                .thenReturn(Optional.of(organization));

        organizationService.deleteOrganization(1L);

        verify(organizationRepository).findById(1L);
        verify(organizationRepository).delete(organization);
    }
}