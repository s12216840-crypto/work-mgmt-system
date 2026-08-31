package com.example.work_management_system.repository;

import com.example.work_management_system.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Long> {
}