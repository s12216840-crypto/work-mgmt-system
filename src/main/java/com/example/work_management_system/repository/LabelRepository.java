
        package com.example.work_management_system.repository;

import com.example.work_management_system.entity.Label;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LabelRepository extends JpaRepository<Label, Long> {

    Optional<Label> findByNameIgnoreCase(String name);

    boolean existsByNameIgnoreCase(String name);
}

