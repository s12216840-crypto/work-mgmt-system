package com.example.work_management_system.service;

import com.example.work_management_system.dto.LabelRequest;
import com.example.work_management_system.dto.LabelResponse;

import java.util.List;

public interface LabelService {

    LabelResponse createLabel(LabelRequest request);

    List<LabelResponse> getAllLabels();

    void deleteLabel(Long labelId);

    List<LabelResponse> getTaskLabels(Long taskId);

    void addLabelToTask(Long taskId, Long labelId);

    void removeLabelFromTask(Long taskId, Long labelId);
}