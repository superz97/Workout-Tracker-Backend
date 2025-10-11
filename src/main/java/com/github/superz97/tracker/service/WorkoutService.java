package com.github.superz97.tracker.service;

import com.github.superz97.tracker.dto.request.WorkoutCreateRequest;
import com.github.superz97.tracker.dto.request.WorkoutUpdateRequest;
import com.github.superz97.tracker.dto.response.WorkoutResponse;
import com.github.superz97.tracker.entity.model.WorkoutStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface WorkoutService {

    WorkoutResponse createWorkout(WorkoutCreateRequest request, Long userId);
    WorkoutResponse getWorkoutById(Long workoutId, Long userId);
    Page<WorkoutResponse> getUserWorkouts(Long userId, WorkoutStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable);
    WorkoutResponse updateWorkout(Long workoutId, WorkoutUpdateRequest request, Long userId);
    void deleteWorkout(Long workoutId, Long userId);
    WorkoutResponse completeWorkout(Long workoutId, Long userId);
    WorkoutResponse cancelWorkout(Long workoutId, Long userId);

}
