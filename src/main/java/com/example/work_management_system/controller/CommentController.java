
        package com.example.work_management_system.controller;

import com.example.work_management_system.dto.CommentRequest;
import com.example.work_management_system.dto.CommentResponse;
import com.example.work_management_system.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tasks")
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<CommentResponse> createComment(
            @PathVariable Long taskId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {

        String userEmail = authentication.getName();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(commentService.createComment(
                        taskId,
                        userEmail,
                        request
                ));
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<CommentResponse>> getTaskComments(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                commentService.getTaskComments(taskId)
        );
    }

    @PutMapping("/comments/{commentId}")
    public ResponseEntity<CommentResponse> updateComment(
            @PathVariable Long commentId,
            @Valid @RequestBody CommentRequest request,
            Authentication authentication) {

        String userEmail = authentication.getName();

        return ResponseEntity.ok(
                commentService.updateComment(
                        commentId,
                        userEmail,
                        request
                )
        );
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(
            @PathVariable Long commentId,
            Authentication authentication) {

        String userEmail = authentication.getName();

        commentService.deleteComment(
                commentId,
                userEmail
        );

        return ResponseEntity.noContent().build();
    }
}

