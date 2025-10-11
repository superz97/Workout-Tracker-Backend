package com.github.superz97.tracker.controller;

import com.github.superz97.tracker.dto.request.WorkoutCreateRequest;
import com.github.superz97.tracker.dto.request.WorkoutUpdateRequest;
import com.github.superz97.tracker.dto.response.ApiResponse;
import com.github.superz97.tracker.dto.response.WorkoutResponse;
import com.github.superz97.tracker.entity.User;
import com.github.superz97.tracker.entity.model.WorkoutStatus;
import com.github.superz97.tracker.service.WorkoutService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/workouts")
@RequiredArgsConstructor
@Tag(name = "workouts", description = "Workout management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class WorkoutController {

    private final WorkoutService workoutService;

    @PostMapping
    @Operation(summary = "Create a new workout", description = "Create a new workout plan with exercises")
    public ResponseEntity<ApiResponse<WorkoutResponse>> createWorkout(
            @Valid @RequestBody WorkoutCreateRequest request,
            @AuthenticationPrincipal User currentUser
            ) {
        WorkoutResponse response = workoutService.createWorkout(request, currentUser.getId());
        return new ResponseEntity<>(
                ApiResponse.success("Workout created successfully", response),
                HttpStatus.CREATED
        );
    }

    @GetMapping
    @Operation(summary = "Get user workouts", description = "Get all workouts for the authenticated user with optional filters")
    public ResponseEntity<ApiResponse<Page<WorkoutResponse>>> getUserWorkouts(
            @Parameter(description = "Filter by workout status")
            @RequestParam(required = false) WorkoutStatus status,
            @Parameter(description = "Filter by start date")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "Filter by end date")
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @PageableDefault(size = 10, sort = "scheduledDate", direction = Sort.Direction.DESC) Pageable pageable,
            @AuthenticationPrincipal User currentUser) {
        Page<WorkoutResponse> workouts = workoutService.getUserWorkouts(
                currentUser.getId(), status, startDate, endDate, pageable);
        return ResponseEntity.ok(ApiResponse.success(workouts));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get workout by ID", description = "Get details of a specific workout")
    public ResponseEntity<ApiResponse<WorkoutResponse>> getWorkoutById(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        WorkoutResponse response = workoutService.getWorkoutById(id, currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success(response));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update workout", description = "Update an existing workout plan")
    public ResponseEntity<ApiResponse<WorkoutResponse>> updateWorkout(
            @PathVariable Long id,
            @Valid @RequestBody WorkoutUpdateRequest request,
            @AuthenticationPrincipal User currentUser) {
        WorkoutResponse response = workoutService.updateWorkout(id, request, currentUser.getId());
        return ResponseEntity.ok(
                ApiResponse.success("Workout updated successfully", response)
        );
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete workout", description = "Delete a workout plan")
    public ResponseEntity<ApiResponse<Void>> deleteWorkout(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        workoutService.deleteWorkout(id, currentUser.getId());
        return ResponseEntity.ok(
                ApiResponse.success("Workout deleted successfully", null)
        );
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Complete workout", description = "Mark a workout as completed")
    public ResponseEntity<ApiResponse<WorkoutResponse>> completeWorkout(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        WorkoutResponse response = workoutService.completeWorkout(id, currentUser.getId());
        return ResponseEntity.ok(
                ApiResponse.success("Workout completed successfully", response)
        );
    }

    @PostMapping("/{id}/cancel")
    @Operation(summary = "Cancel workout", description = "Cancel a scheduled workout")
    public ResponseEntity<ApiResponse<WorkoutResponse>> cancelWorkout(
            @PathVariable Long id,
            @AuthenticationPrincipal User currentUser) {
        WorkoutResponse response = workoutService.cancelWorkout(id, currentUser.getId());
        return ResponseEntity.ok(
                ApiResponse.success("Workout cancelled successfully", response)
        );
    }

}
