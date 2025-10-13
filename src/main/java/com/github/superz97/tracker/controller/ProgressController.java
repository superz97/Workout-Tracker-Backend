package com.github.superz97.tracker.controller;

import com.github.superz97.tracker.dto.response.ApiResponse;
import com.github.superz97.tracker.dto.response.ProgressResponse;
import com.github.superz97.tracker.dto.response.ReportResponse;
import com.github.superz97.tracker.entity.User;
import com.github.superz97.tracker.service.ProgressService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Progress & Reports", description = "Progress tracking and reporting APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class ProgressController {

    private final ProgressService progressService;

    @GetMapping("/progress/summary")
    @Operation(summary = "Get progress summary", description = "Get overall progress summary for the authenticated user")
    public ResponseEntity<ApiResponse<ProgressResponse>> getProgressSummary(
            @AuthenticationPrincipal User currentUser) {
        ProgressResponse progress = progressService.getUserProgressSummary(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(progress));
    }

    @GetMapping("/progress/exercise/{exerciseId}")
    @Operation(summary = "Get exercise progress", description = "Get progress for a specific exercise")
    public ResponseEntity<ApiResponse<ProgressResponse>> getExerciseProgress(
            @PathVariable Long exerciseId,
            @AuthenticationPrincipal User currentUser) {
        ProgressResponse progress = progressService.getExerciseProgress(currentUser.getId(), exerciseId);
        return ResponseEntity.ok(ApiResponse.success(progress));
    }

    @GetMapping("/reports/weekly")
    @Operation(summary = "Get weekly report", description = "Generate a weekly workout report")
    public ResponseEntity<ApiResponse<ReportResponse>> getWeeklyReport(
            @Parameter(description = "Week start date (defaults to current week)")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate weekStart,
            @AuthenticationPrincipal User currentUser) {
        if (weekStart == null) {
            // Default to current week's Monday
            weekStart = LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue() - 1);
        }
        ReportResponse report = progressService.getWeeklyReport(currentUser.getId(), weekStart);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/reports/monthly")
    @Operation(summary = "Get monthly report", description = "Generate a monthly workout report")
    public ResponseEntity<ApiResponse<ReportResponse>> getMonthlyReport(
            @Parameter(description = "Year (defaults to current year)")
            @RequestParam(required = false) Integer year,
            @Parameter(description = "Month (1-12, defaults to current month)")
            @RequestParam(required = false) Integer month,
            @AuthenticationPrincipal User currentUser) {
        if (year == null) {
            year = LocalDate.now().getYear();
        }
        if (month == null) {
            month = LocalDate.now().getMonthValue();
        }
        ReportResponse report = progressService.getMonthlyReport(currentUser.getId(), year, month);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

    @GetMapping("/reports/custom")
    @Operation(summary = "Get custom report", description = "Generate a custom date range workout report")
    public ResponseEntity<ApiResponse<ReportResponse>> getCustomReport(
            @Parameter(description = "Start date", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @AuthenticationPrincipal User currentUser) {
        ReportResponse report = progressService.getCustomReport(currentUser.getId(), startDate, endDate);
        return ResponseEntity.ok(ApiResponse.success(report));
    }

}
