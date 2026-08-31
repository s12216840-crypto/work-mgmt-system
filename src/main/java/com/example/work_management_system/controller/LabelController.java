package com.example.work_management_system.controller;

import com.example.work_management_system.dto.LabelRequest;
import com.example.work_management_system.dto.LabelResponse;
import com.example.work_management_system.service.LabelService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class LabelController {

    private final LabelService labelService;

    @PostMapping("/labels")
    public ResponseEntity<LabelResponse> createLabel(
            @Valid @RequestBody LabelRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(labelService.createLabel(request));
    }

    @GetMapping("/labels")
    public ResponseEntity<List<LabelResponse>> getAllLabels() {

        return ResponseEntity.ok(
                labelService.getAllLabels()
        );
    }

    @DeleteMapping("/labels/{labelId}")
    public ResponseEntity<Void> deleteLabel(
            @PathVariable Long labelId) {

        labelService.deleteLabel(labelId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/tasks/{taskId}/labels")
    public ResponseEntity<List<LabelResponse>> getTaskLabels(
            @PathVariable Long taskId) {

        return ResponseEntity.ok(
                labelService.getTaskLabels(taskId)
        );
    }

    @PostMapping("/tasks/{taskId}/labels/{labelId}")
    public ResponseEntity<Void> addLabelToTask(
            @PathVariable Long taskId,
            @PathVariable Long labelId) {

        labelService.addLabelToTask(
                taskId,
                labelId
        );

        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/tasks/{taskId}/labels/{labelId}")
    public ResponseEntity<Void> removeLabelFromTask(
            @PathVariable Long taskId,
            @PathVariable Long labelId) {

        labelService.removeLabelFromTask(
                taskId,
                labelId
        );

        return ResponseEntity.noContent().build();
    }
}