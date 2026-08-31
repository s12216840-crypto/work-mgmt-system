
        package com.example.work_management_system.service.impl;

import com.example.work_management_system.dto.CommentRequest;
import com.example.work_management_system.dto.CommentResponse;
import com.example.work_management_system.entity.Comment;
import com.example.work_management_system.entity.Task;
import com.example.work_management_system.entity.User;
import com.example.work_management_system.exception.TaskNotFoundException;
import com.example.work_management_system.repository.CommentRepository;
import com.example.work_management_system.repository.TaskRepository;
import com.example.work_management_system.repository.UserRepository;
import com.example.work_management_system.service.CommentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    @Override
    public CommentResponse createComment(
            Long taskId,
            String userEmail,
            CommentRequest request) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + taskId));

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .task(task)
                .user(user)
                .build();

        return mapToResponse(
                commentRepository.save(comment)
        );
    }

    @Override
    public List<CommentResponse> getTaskComments(Long taskId) {

        if (!taskRepository.existsById(taskId)) {
            throw new TaskNotFoundException(
                    "Task not found with id: " + taskId);
        }

        return commentRepository
                .findByTaskIdOrderByCreatedAtAsc(taskId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public CommentResponse updateComment(
            Long commentId,
            String userEmail,
            CommentRequest request) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Comment comment = commentRepository
                .findByIdAndUserId(commentId, user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Comment not found or you are not the owner"));

        comment.setContent(request.getContent());

        return mapToResponse(
                commentRepository.save(comment)
        );
    }

    @Override
    public void deleteComment(
            Long commentId,
            String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Comment comment = commentRepository
                .findByIdAndUserId(commentId, user.getId())
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Comment not found or you are not the owner"));

        commentRepository.delete(comment);
    }

    private CommentResponse mapToResponse(Comment comment) {

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .taskId(comment.getTask().getId())
                .userId(comment.getUser().getId())
                .userName(comment.getUser().getName())
                .createdAt(comment.getCreatedAt())
                .updatedAt(comment.getUpdatedAt())
                .build();
    }
}
