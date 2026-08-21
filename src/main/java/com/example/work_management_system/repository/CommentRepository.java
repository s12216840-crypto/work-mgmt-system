package com.example.work_management_system.repository;

import com.example.work_management_system.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findByTaskIdOrderByCreatedAtAsc(Long taskId);

    Optional<Comment> findByIdAndUserId(Long commentId, Long userId);
}