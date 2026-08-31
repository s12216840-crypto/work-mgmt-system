
        package com.example.work_management_system.service;

import com.example.work_management_system.dto.CommentRequest;
import com.example.work_management_system.dto.CommentResponse;

import java.util.List;

public interface CommentService {

    CommentResponse createComment(
            Long taskId,
            String userId,
            CommentRequest request
    );

    List<CommentResponse> getTaskComments(Long taskId);

    CommentResponse updateComment(
            Long commentId,
            String userId,
            CommentRequest request
    );

    void deleteComment(
            Long commentId,
            String userId
    );
}

