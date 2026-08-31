package com.example.work_management_system.repository;

import com.example.work_management_system.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrganizationRepository extends JpaRepository<Organization, Long> {
}