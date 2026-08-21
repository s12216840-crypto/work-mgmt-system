package com.example.work_management_system.service.impl;

import com.example.work_management_system.dto.LabelRequest;
import com.example.work_management_system.dto.LabelResponse;
import com.example.work_management_system.entity.Label;
import com.example.work_management_system.entity.Task;
import com.example.work_management_system.exception.TaskNotFoundException;
import com.example.work_management_system.repository.LabelRepository;
import com.example.work_management_system.repository.TaskRepository;
import com.example.work_management_system.service.LabelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final TaskRepository taskRepository;

    @Override
    public LabelResponse createLabel(LabelRequest request) {

        if (labelRepository.existsByNameIgnoreCase(request.getName())) {
            throw new IllegalArgumentException(
                    "Label already exists");
        }

        Label label = Label.builder()
                .name(request.getName().trim())
                .build();

        return mapToResponse(
                labelRepository.save(label)
        );
    }

    @Override
    public List<LabelResponse> getAllLabels() {

        return labelRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void deleteLabel(Long labelId) {

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Label not found with id: " + labelId));

        labelRepository.delete(label);
    }

    @Override
    public List<LabelResponse> getTaskLabels(Long taskId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + taskId));

        return task.getLabels()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Override
    public void addLabelToTask(
            Long taskId,
            Long labelId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + taskId));

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Label not found with id: " + labelId));

        if (!task.getLabels().contains(label)) {
            task.getLabels().add(label);
            taskRepository.save(task);
        }
    }

    @Override
    public void removeLabelFromTask(
            Long taskId,
            Long labelId) {

        Task task = taskRepository.findById(taskId)
                .orElseThrow(() ->
                        new TaskNotFoundException(
                                "Task not found with id: " + taskId));

        Label label = labelRepository.findById(labelId)
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Label not found with id: " + labelId));

        task.getLabels().remove(label);

        taskRepository.save(task);
    }

    private LabelResponse mapToResponse(Label label) {

        return LabelResponse.builder()
                .id(label.getId())
                .name(label.getName())
                .build();
    }
}