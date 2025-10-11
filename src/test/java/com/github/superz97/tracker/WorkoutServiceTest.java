package com.github.superz97.tracker;

import com.github.superz97.tracker.dto.request.WorkoutCreateRequest;
import com.github.superz97.tracker.dto.request.WorkoutExerciseDto;
import com.github.superz97.tracker.dto.response.WorkoutResponse;
import com.github.superz97.tracker.entity.Exercise;
import com.github.superz97.tracker.entity.User;
import com.github.superz97.tracker.entity.WorkoutPlan;
import com.github.superz97.tracker.entity.model.ExerciseCategory;
import com.github.superz97.tracker.entity.model.MuscleGroup;
import com.github.superz97.tracker.entity.model.WorkoutStatus;
import com.github.superz97.tracker.exception.ResourceNotFoundException;
import com.github.superz97.tracker.mapper.WorkoutMapper;
import com.github.superz97.tracker.repository.ExerciseRepository;
import com.github.superz97.tracker.repository.UserRepository;
import com.github.superz97.tracker.repository.WorkoutExerciseRepository;
import com.github.superz97.tracker.repository.WorkoutPlanRepository;
import com.github.superz97.tracker.service.impl.WorkoutServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WorkoutServiceTest {

    @Mock
    private WorkoutPlanRepository workoutPlanRepository;

    @Mock
    private WorkoutExerciseRepository workoutExerciseRepository;

    @Mock
    private ExerciseRepository exerciseRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkoutMapper workoutMapper;

    @InjectMocks
    private WorkoutServiceImpl workoutService;

    private User testUser;
    private Exercise testExercise;
    private WorkoutPlan testWorkoutPlan;
    private WorkoutCreateRequest createRequest;
    private WorkoutResponse workoutResponse;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1L)
                .username("testuser")
                .email("test@example.com")
                .isActive(true)
                .build();
        testExercise = Exercise.builder()
                .id(1L)
                .name("Bench Press")
                .category(ExerciseCategory.STRENGTH)
                .muscleGroup(MuscleGroup.CHEST)
                .build();
        testWorkoutPlan = WorkoutPlan.builder()
                .id(1L)
                .user(testUser)
                .name("Upper Body Workout")
                .scheduledDate(LocalDate.now().plusDays(1))
                .scheduledTime(LocalTime.of(10, 0))
                .status(WorkoutStatus.SCHEDULED)
                .build();
        WorkoutExerciseDto exerciseDto = WorkoutExerciseDto.builder()
                .exerciseId(1L)
                .sets(3)
                .reps(10)
                .weight(BigDecimal.valueOf(60))
                .restSeconds(90)
                .build();
        createRequest = WorkoutCreateRequest.builder()
                .name("Upper Body Workout")
                .description("Chest and shoulders focus")
                .scheduledDate(LocalDate.now().plusDays(1))
                .scheduledTime(LocalTime.of(10, 0))
                .exercises(Arrays.asList(exerciseDto))
                .build();
        workoutResponse = WorkoutResponse.builder()
                .id(1L)
                .name("Upper Body Workout")
                .status("SCHEDULED")
                .build();
    }

    @Test
    void createWorkout_ShouldCreateSuccessfully() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(1L)).thenReturn(Optional.of(testExercise));
        when(workoutPlanRepository.save(any(WorkoutPlan.class))).thenReturn(testWorkoutPlan);
        when(workoutMapper.toWorkoutResponse(any(WorkoutPlan.class))).thenReturn(workoutResponse);
        // When
        WorkoutResponse result = workoutService.createWorkout(createRequest, 1L);
        // Then
        assertNotNull(result);
        assertEquals("Upper Body Workout", result.getName());
        verify(workoutPlanRepository, times(2)).save(any(WorkoutPlan.class));
        verify(exerciseRepository).findById(1L);
    }

    @Test
    void createWorkout_ShouldThrowException_WhenUserNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> workoutService.createWorkout(createRequest, 1L));
        verify(workoutPlanRepository, never()).save(any());
    }

    @Test
    void createWorkout_ShouldThrowException_WhenExerciseNotFound() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(testUser));
        when(exerciseRepository.findById(1L)).thenReturn(Optional.empty());
        when(workoutPlanRepository.save(any(WorkoutPlan.class))).thenReturn(testWorkoutPlan);
        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> workoutService.createWorkout(createRequest, 1L));
    }

    @Test
    void getWorkoutById_ShouldReturnWorkout_WhenExists() {
        // Given
        when(workoutPlanRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testWorkoutPlan));
        when(workoutMapper.toWorkoutResponse(testWorkoutPlan)).thenReturn(workoutResponse);
        // When
        WorkoutResponse result = workoutService.getWorkoutById(1L, 1L);
        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(workoutPlanRepository).findByIdAndUserId(1L, 1L);
    }

    @Test
    void getWorkoutById_ShouldThrowException_WhenNotFound() {
        // Given
        when(workoutPlanRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.empty());
        // When & Then
        assertThrows(ResourceNotFoundException.class,
                () -> workoutService.getWorkoutById(1L, 1L));
    }

    @Test
    void completeWorkout_ShouldUpdateStatus() {
        // Given
        when(workoutPlanRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testWorkoutPlan));
        when(workoutPlanRepository.save(any(WorkoutPlan.class))).thenReturn(testWorkoutPlan);
        when(workoutMapper.toWorkoutResponse(any(WorkoutPlan.class))).thenReturn(workoutResponse);
        // When
        WorkoutResponse result = workoutService.completeWorkout(1L, 1L);
        // Then
        assertNotNull(result);
        assertEquals(WorkoutStatus.COMPLETED, testWorkoutPlan.getStatus());
        assertNotNull(testWorkoutPlan.getCompletedAt());
        verify(workoutPlanRepository).save(testWorkoutPlan);
    }

    @Test
    void deleteWorkout_ShouldDeleteSuccessfully() {
        // Given
        when(workoutPlanRepository.findByIdAndUserId(1L, 1L)).thenReturn(Optional.of(testWorkoutPlan));
        // When
        workoutService.deleteWorkout(1L, 1L);
        // Then
        verify(workoutPlanRepository).delete(testWorkoutPlan);
    }

}
