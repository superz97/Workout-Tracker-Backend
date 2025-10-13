package com.github.superz97.tracker.controller;

import com.github.superz97.tracker.dto.request.LogEntryRequest;
import com.github.superz97.tracker.dto.response.ApiResponse;
import com.github.superz97.tracker.dto.response.WorkoutLogResponse;
import com.github.superz97.tracker.entity.User;
import com.github.superz97.tracker.service.WorkoutLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logs")
@RequiredArgsConstructor
@Tag(name = "Workout Logs", description = "Workout logging APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class WorkoutLogController {

    private final WorkoutLogService workoutLogService;

    @PostMapping
    @Operation(summary = "Create workout log", description = "Log a completed exercise set")
    public ResponseEntity<ApiResponse<WorkoutLogResponse>> createLog(
            @Valid @RequestBody LogEntryRequest request,
            @AuthenticationPrincipal User currentUser) {
        WorkoutLogResponse response = workoutLogService.createLog(request, currentUser.getId());
        return new ResponseEntity<>(
                ApiResponse.success("Workout log created successfully", response),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get user logs", description = "Get all workout logs for the authenticated user")
    public ResponseEntity<ApiResponse<Page<WorkoutLogResponse>>> getUserLogs(
            @PageableDefault(size = 20, sort = "loggedAt", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        Page<WorkoutLogResponse> logs = workoutLogService.getUserLogs(currentUser.getId(), pageable);
        return ResponseEntity.ok(ApiResponse.success(logs));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update workout log", description = "Update an existing workout log entry")
    public ResponseEntity<ApiResponse<WorkoutLogResponse>> updateLog(
            @PathVariable Long id,
            @Valid @RequestBody LogEntryRequest request,
            @AuthenticationPrincipal User currentUser) {
        WorkoutLogResponse response = workoutLogService.updateLog(id, request, currentUser.getId());
        return ResponseEntity.ok(
                ApiResponse.success("Workout log updated successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete workout log", description = "Delete a workout log entry")
    public ResponseEntity<ApiResponse<Void>> deleteLog(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        workoutLogService.deleteLog(id, currentUser.getId());
        return ResponseEntity.ok(
                ApiResponse.success("Workout log deleted successfully", null)
        );
    }

}
