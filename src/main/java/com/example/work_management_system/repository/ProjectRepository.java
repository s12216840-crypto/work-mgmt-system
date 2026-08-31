package com.example.work_management_system.repository;

import com.example.work_management_system.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
}