package com.github.superz97.tracker.service.impl;

import com.github.superz97.tracker.dto.request.WorkoutCreateRequest;
import com.github.superz97.tracker.dto.request.WorkoutExerciseDto;
import com.github.superz97.tracker.dto.request.WorkoutUpdateRequest;
import com.github.superz97.tracker.dto.response.WorkoutResponse;
import com.github.superz97.tracker.entity.Exercise;
import com.github.superz97.tracker.entity.User;
import com.github.superz97.tracker.entity.WorkoutExercise;
import com.github.superz97.tracker.entity.WorkoutPlan;
import com.github.superz97.tracker.entity.model.WorkoutStatus;
import com.github.superz97.tracker.exception.BadRequestException;
import com.github.superz97.tracker.exception.ResourceNotFoundException;
import com.github.superz97.tracker.mapper.WorkoutMapper;
import com.github.superz97.tracker.repository.ExerciseRepository;
import com.github.superz97.tracker.repository.UserRepository;
import com.github.superz97.tracker.repository.WorkoutExerciseRepository;
import com.github.superz97.tracker.repository.WorkoutPlanRepository;
import com.github.superz97.tracker.service.WorkoutService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class WorkoutServiceImpl implements WorkoutService {

    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutExerciseRepository workoutExerciseRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final WorkoutMapper workoutMapper;

    @Override
    @Transactional
    public WorkoutResponse createWorkout(WorkoutCreateRequest request, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        WorkoutPlan workoutPlan = WorkoutPlan.builder()
                .user(user)
                .name(request.getName())
                .description(request.getDescription())
                .scheduledDate(request.getScheduledDate())
                .scheduledTime(request.getScheduledTime())
                .status(WorkoutStatus.SCHEDULED)
                .notes(request.getNotes())
                .build();
        workoutPlan = workoutPlanRepository.save(workoutPlan);

        for (int i = 0; i < request.getExercises().size(); i++) {
            WorkoutExerciseDto exerciseDto = request.getExercises().get(i);
            Exercise exercise = exerciseRepository.findById(exerciseDto.getExerciseId())
                    .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id: " + exerciseDto.getExerciseId()));
            WorkoutExercise workoutExercise = WorkoutExercise.builder()
                    .workoutPlan(workoutPlan)
                    .exercise(exercise)
                    .sets(exerciseDto.getSets())
                    .reps(exerciseDto.getReps())
                    .weight(exerciseDto.getWeight())
                    .restSeconds(exerciseDto.getRestSeconds())
                    .orderIndex(exerciseDto.getOrderIndex() != null ? exerciseDto.getOrderIndex() : i + 1)
                    .notes(exerciseDto.getNotes())
                    .build();
            workoutPlan.addExercise(workoutExercise);
        }

        workoutPlan = workoutPlanRepository.save(workoutPlan);
        log.info("Created new workout plan: {} for user: {}", workoutPlan.getId(), userId);
        return workoutMapper.toWorkoutResponse(workoutPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public WorkoutResponse getWorkoutById(Long workoutId, Long userId) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));
        return workoutMapper.toWorkoutResponse(workoutPlan);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<WorkoutResponse> getUserWorkouts(Long userId, WorkoutStatus status, LocalDate startDate, LocalDate endDate, Pageable pageable) {
        Page<WorkoutPlan> workouts = workoutPlanRepository.findByUserIdWithFilters(
                userId, status, startDate, endDate, pageable);
        return workouts.map(workoutMapper::toWorkoutResponse);
    }

    @Override
    @Transactional
    public WorkoutResponse updateWorkout(Long workoutId, WorkoutUpdateRequest request, Long userId) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));
        if (workoutPlan.getStatus() == WorkoutStatus.COMPLETED) {
            throw new BadRequestException("Cannot update a completed workout");
        }
        // Update fields if provided
        if (request.getName() != null) {
            workoutPlan.setName(request.getName());
        }
        if (request.getDescription() != null) {
            workoutPlan.setDescription(request.getDescription());
        }
        if (request.getScheduledDate() != null) {
            workoutPlan.setScheduledDate(request.getScheduledDate());
        }
        if (request.getScheduledTime() != null) {
            workoutPlan.setScheduledTime(request.getScheduledTime());
        }
        if (request.getDurationMinutes() != null) {
            workoutPlan.setDurationMinutes(request.getDurationMinutes());
        }
        if (request.getNotes() != null) {
            workoutPlan.setNotes(request.getNotes());
        }
        if (request.getStatus() != null) {
            try {
                WorkoutStatus newStatus = WorkoutStatus.valueOf(request.getStatus().toUpperCase());
                workoutPlan.setStatus(newStatus);
            } catch (IllegalArgumentException e) {
                throw new BadRequestException("Invalid workout status: " + request.getStatus());
            }
        }
        workoutPlan = workoutPlanRepository.save(workoutPlan);
        log.info("Updated workout plan: {} for user: {}", workoutId, userId);
        return workoutMapper.toWorkoutResponse(workoutPlan);
    }

    @Override
    @Transactional
    public void deleteWorkout(Long workoutId, Long userId) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));
        workoutPlanRepository.delete(workoutPlan);
        log.info("Deleted workout plan: {} for user: {}", workoutId, userId);
    }

    @Override
    @Transactional
    public WorkoutResponse completeWorkout(Long workoutId, Long userId) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));
        if (workoutPlan.getStatus() == WorkoutStatus.COMPLETED) {
            throw new BadRequestException("Workout is already completed");
        }
        workoutPlan.setStatus(WorkoutStatus.COMPLETED);
        workoutPlan.setCompletedAt(LocalDateTime.now());
        workoutPlan = workoutPlanRepository.save(workoutPlan);
        log.info("Completed workout plan: {} for user: {}", workoutId, userId);
        return workoutMapper.toWorkoutResponse(workoutPlan);
    }

    @Override
    @Transactional
    public WorkoutResponse cancelWorkout(Long workoutId, Long userId) {
        WorkoutPlan workoutPlan = workoutPlanRepository.findByIdAndUserId(workoutId, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Workout not found"));
        if (workoutPlan.getStatus() == WorkoutStatus.COMPLETED) {
            throw new BadRequestException("Cannot cancel a completed workout");
        }
        workoutPlan.setStatus(WorkoutStatus.CANCELLED);
        workoutPlan = workoutPlanRepository.save(workoutPlan);
        log.info("Cancelled workout plan: {} for user: {}", workoutId, userId);
        return workoutMapper.toWorkoutResponse(workoutPlan);
    }

}
