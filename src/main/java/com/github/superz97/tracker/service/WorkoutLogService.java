package com.github.superz97.tracker.service;

import com.github.superz97.tracker.dto.request.LogEntryRequest;
import com.github.superz97.tracker.dto.response.WorkoutLogResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface WorkoutLogService {

    WorkoutLogResponse createLog(LogEntryRequest request, Long userId);
    Page<WorkoutLogResponse> getUserLogs(Long userId, Pageable pageable);
    WorkoutLogResponse updateLog(Long logId, LogEntryRequest request, Long userId);
    void deleteLog(Long logId, Long userId);

}
