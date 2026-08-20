package com.example.work_management_system.repository;

import com.example.work_management_system.entity.Task;
import com.example.work_management_system.entity.TaskPriority;
import com.example.work_management_system.entity.TaskStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TaskRepository extends JpaRepository<Task, Long> {

    @Query("""
            SELECT t
            FROM Task t
            WHERE (:status IS NULL OR t.status = :status)
            AND (:priority IS NULL OR t.priority = :priority)
            AND (:assigneeId IS NULL OR t.assignee.id = :assigneeId)
            AND (:projectId IS NULL OR t.project.id = :projectId)
            AND (
                :search IS NULL
                OR LOWER(t.title) LIKE LOWER(CONCAT('%', :search, '%'))
                OR LOWER(t.description) LIKE LOWER(CONCAT('%', :search, '%'))
            )
            """)
    Page<Task> searchTasks(
            @Param("status") TaskStatus status,
            @Param("priority") TaskPriority priority,
            @Param("assigneeId") Long assigneeId,
            @Param("projectId") Long projectId,
            @Param("search") String search,
            Pageable pageable
    );
}