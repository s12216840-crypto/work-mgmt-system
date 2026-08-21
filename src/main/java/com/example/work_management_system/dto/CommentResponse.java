package com.example.work_management_system.dto;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class CommentResponse {

    private Long id;

    private String content;

    private Long taskId;

    private Long userId;

    private String userName;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}