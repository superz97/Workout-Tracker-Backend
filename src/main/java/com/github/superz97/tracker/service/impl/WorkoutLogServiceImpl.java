package com.github.superz97.tracker.service.impl;

import com.github.superz97.tracker.dto.request.LogEntryRequest;
import com.github.superz97.tracker.dto.response.WorkoutLogResponse;
import com.github.superz97.tracker.entity.*;
import com.github.superz97.tracker.exception.BadRequestException;
import com.github.superz97.tracker.exception.ResourceNotFoundException;
import com.github.superz97.tracker.repository.*;
import com.github.superz97.tracker.service.WorkoutLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutLogServiceImpl implements WorkoutLogService {

    private final WorkoutLogRepository workoutLogRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final UserProgressRepository userProgressRepository;

    @Override
    @Transactional
    public WorkoutLogResponse createLog(LogEntryRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        Exercise exercise = exerciseRepository.findById(request.getExerciseId())
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));
        WorkoutLog.WorkoutLogBuilder logBuilder = WorkoutLog.builder()
                .user(user)
                .exercise(exercise)
                .actualSets(request.getActualSets())
                .actualReps(request.getActualReps())
                .actualWeight(request.getActualWeight())
                .notes(request.getNotes());
        // Optional: Link to workout plan
        if (request.getWorkoutPlanId() != null) {
            WorkoutPlan workoutPlan = workoutPlanRepository.findByIdAndUserId(request.getWorkoutPlanId(), userId)
                    .orElseThrow(() -> new ResourceNotFoundException("Workout plan not found"));
            logBuilder.workoutPlan(workoutPlan);
        }
        // Optional: Link to specific workout exercise
        if (request.getWorkoutExerciseId() != null) {
            WorkoutExercise workoutExercise = workoutExerciseRepository.findById(request.getWorkoutExerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Workout exercise not found"));
            logBuilder.workoutExercise(workoutExercise);
        }
        WorkoutLog workoutLog = logBuilder.build();
        workoutLog = workoutLogRepository.save(workoutLog);
        // Update user progress
        updateUserProgress(user, exercise, request);
        log.info("Created workout log: {} for user: {}", workoutLog.getId(), userId);
        return mapToResponse(workoutLog);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutLogResponse> getUserLogs(Long userId, Pageable pageable) {
        Page<WorkoutLog> logs = workoutLogRepository.findByUserId(userId, pageable);
        return logs.map(this::mapToResponse);
    }

    @Override
    @Transactional
    public WorkoutLogResponse updateLog(Long logId, LogEntryRequest request, Long userId) {
        WorkoutLog workoutLog = workoutLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout log not found"));
        if (!workoutLog.getUser().getId().equals(userId)) {
            throw new BadRequestException("You can only update your own logs");
        }
        // Update fields
        workoutLog.setActualSets(request.getActualSets());
        workoutLog.setActualReps(request.getActualReps());
        workoutLog.setActualWeight(request.getActualWeight());
        workoutLog.setNotes(request.getNotes());
        workoutLog = workoutLogRepository.save(workoutLog);
        // Update user progress
        updateUserProgress(workoutLog.getUser(), workoutLog.getExercise(), request);
        log.info("Updated workout log: {} for user: {}", logId, userId);
        return mapToResponse(workoutLog);
    }

    @Override
    @Transactional
    public void deleteLog(Long logId, Long userId) {
        WorkoutLog workoutLog = workoutLogRepository.findById(logId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout log not found"));
        if (!workoutLog.getUser().getId().equals(userId)) {
            throw new BadRequestException("You can only delete your own logs");
        }
        workoutLogRepository.delete(workoutLog);
        log.info("Deleted workout log: {} for user: {}", logId, userId);
    }

    private void updateUserProgress(User user, Exercise exercise, LogEntryRequest request) {
        UserProgress progress = userProgressRepository.findByUserIdAndExerciseId(user.getId(), exercise.getId())
                .orElse(UserProgress.builder()
                        .user(user)
                        .exercise(exercise)
                        .maxWeight(BigDecimal.ZERO)
                        .maxReps(0)
                        .totalVolume(BigDecimal.ZERO)
                        .build());
        // Update max weight
        if (request.getActualWeight() != null &&
                request.getActualWeight().compareTo(progress.getMaxWeight() == null ? BigDecimal.ZERO : progress.getMaxWeight()) > 0) {
            progress.setMaxWeight(request.getActualWeight());
        }
        // Update max reps
        if (request.getActualReps() != null &&
                request.getActualReps() > (progress.getMaxReps() == null ? 0 : progress.getMaxReps())) {
            progress.setMaxReps(request.getActualReps());
        }
        // Update total volume
        BigDecimal sessionVolume = calculateVolume(request);
        BigDecimal currentTotal = progress.getTotalVolume() == null ? BigDecimal.ZERO : progress.getTotalVolume();
        progress.setTotalVolume(currentTotal.add(sessionVolume));
        // Update last performed
        progress.setLastPerformed(LocalDateTime.now());
        userProgressRepository.save(progress);
    }

    private BigDecimal calculateVolume(LogEntryRequest request) {
        if (request.getActualWeight() == null || request.getActualReps() == null || request.getActualSets() == null) {
            return BigDecimal.ZERO;
        }
        return request.getActualWeight()
                .multiply(BigDecimal.valueOf(request.getActualReps()))
                .multiply(BigDecimal.valueOf(request.getActualSets()));
    }

    private WorkoutLogResponse mapToResponse(WorkoutLog log) {
        WorkoutLogResponse.WorkoutLogResponseBuilder builder = WorkoutLogResponse.builder()
                .id(log.getId())
                .exerciseId(log.getExercise().getId())
                .exerciseName(log.getExercise().getName())
                .actualSets(log.getActualSets())
                .actualReps(log.getActualReps())
                .actualWeight(log.getActualWeight())
                .loggedAt(log.getLoggedAt())
                .notes(log.getNotes());
        // Add exercise details
        if (log.getExercise().getCategory() != null) {
            builder.exerciseCategory(log.getExercise().getCategory().getDisplayName());
        }
        if (log.getExercise().getMuscleGroup() != null) {
            builder.muscleGroup(log.getExercise().getMuscleGroup().getDisplayName());
        }
        // Add workout plan details if linked
        if (log.getWorkoutPlan() != null) {
            builder.workoutPlanId(log.getWorkoutPlan().getId())
                    .workoutPlanName(log.getWorkoutPlan().getName());
        }
        // Calculate volume
        BigDecimal volume = BigDecimal.ZERO;
        if (log.getActualWeight() != null && log.getActualReps() != null && log.getActualSets() != null) {
            volume = log.getActualWeight()
                    .multiply(BigDecimal.valueOf(log.getActualReps()))
                    .multiply(BigDecimal.valueOf(log.getActualSets()));
        }
        builder.volume(volume);
        return builder.build();
    }

}
