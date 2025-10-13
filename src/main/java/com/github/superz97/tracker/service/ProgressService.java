package com.github.superz97.tracker.service;

import com.github.superz97.tracker.dto.response.ProgressResponse;
import com.github.superz97.tracker.dto.response.ReportResponse;

import java.time.LocalDate;

public interface ProgressService {
    ProgressResponse getUserProgressSummary(Long userId);
    ProgressResponse getExerciseProgress(Long userId, Long exerciseId);
    ReportResponse getWeeklyReport(Long userId, LocalDate weekStart);
    ReportResponse getMonthlyReport(Long userId, Integer year, Integer month);
    ReportResponse getCustomReport(Long userId, LocalDate startDate, LocalDate endDate);
}
