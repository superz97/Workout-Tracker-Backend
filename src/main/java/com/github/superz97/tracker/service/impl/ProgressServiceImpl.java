package com.github.superz97.tracker.service.impl;

import com.github.superz97.tracker.dto.response.*;
import com.github.superz97.tracker.entity.Exercise;
import com.github.superz97.tracker.entity.UserProgress;
import com.github.superz97.tracker.entity.WorkoutLog;
import com.github.superz97.tracker.entity.WorkoutPlan;
import com.github.superz97.tracker.entity.model.WorkoutStatus;
import com.github.superz97.tracker.exception.ResourceNotFoundException;
import com.github.superz97.tracker.repository.*;
import com.github.superz97.tracker.service.ProgressService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProgressServiceImpl implements ProgressService {

    private final UserProgressRepository userProgressRepository;
    private final WorkoutPlanRepository workoutPlanRepository;
    private final WorkoutLogRepository workoutLogRepository;
    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public ProgressResponse getUserProgressSummary(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found");
        }
        List<UserProgress> progressRecords = userProgressRepository.findByUserId(userId);
        List<WorkoutPlan> completedWorkouts = workoutPlanRepository.findByUserIdAndStatus(
                userId, WorkoutStatus.COMPLETED
        );
        BigDecimal totalVolume = progressRecords.stream()
                .map(UserProgress::getTotalVolume)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        List<ExerciseProgressDto> favoriteExercises = userProgressRepository
                .findTopExercisesByVolume(userId, 5)
                .stream()
                .map(this::mapToExerciseProgressDto)
                .collect(Collectors.toList());
        int currentStreak = calculateCurrentStreak(userId);
        // Calculate weekly average
        WeeklyAverageDto weeklyAverage = calculateWeeklyAverage(userId);
        return ProgressResponse.builder()
                .totalWorkouts(completedWorkouts.size())
                .currentStreak(currentStreak)
                .totalVolume(totalVolume)
                .favoriteExercises(favoriteExercises)
                .weeklyAverage(weeklyAverage)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ProgressResponse getExerciseProgress(Long userId, Long exerciseId) {
        // Verify exercise exists
        Exercise exercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found"));
        // Get user progress for this exercise
        UserProgress progress = userProgressRepository.findByUserIdAndExerciseId(userId, exerciseId)
                .orElse(null);
        if (progress == null) {
            return ProgressResponse.builder()
                    .message("No progress recorded for this exercise")
                    .build();
        }
        // Get workout logs for this exercise
        List<WorkoutLog> logs = workoutLogRepository.findByUserIdAndExerciseId(userId, exerciseId);
        // Calculate progress over time
        List<ProgressDataPoint> progressHistory = logs.stream()
                .map(log -> ProgressDataPoint.builder()
                        .date(log.getLoggedAt().toLocalDate())
                        .weight(log.getActualWeight())
                        .reps(log.getActualReps())
                        .sets(log.getActualSets())
                        .volume(calculateVolume(log))
                        .build())
                .collect(Collectors.toList());
        return ProgressResponse.builder()
                .exerciseName(exercise.getName())
                .maxWeight(progress.getMaxWeight())
                .maxReps(progress.getMaxReps())
                .totalVolume(progress.getTotalVolume())
                .lastPerformed(progress.getLastPerformed())
                .progressHistory(progressHistory)
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponse getWeeklyReport(Long userId, LocalDate weekStart) {
        LocalDate weekEnd = weekStart.plusDays(6);
        return generateReport(userId, weekStart, weekEnd, "Weekly");
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponse getMonthlyReport(Long userId, Integer year, Integer month) {
        LocalDate monthStart = LocalDate.of(year, month, 1);
        LocalDate monthEnd = monthStart.plusMonths(1).minusDays(1);
        return generateReport(userId, monthStart, monthEnd, "Monthly");
    }

    @Override
    @Transactional(readOnly = true)
    public ReportResponse getCustomReport(Long userId, LocalDate startDate, LocalDate endDate) {
        return generateReport(userId, startDate, endDate, "Custom");
    }

    private ReportResponse generateReport(Long userId, LocalDate startDate, LocalDate endDate, String reportType) {
        // Get workouts in date range
        List<WorkoutPlan> workouts = workoutPlanRepository.findByUserIdWithFilters(
                userId, null, startDate, endDate, null
        ).getContent();
        // Get workout logs in date range
        LocalDateTime startDateTime = startDate.atStartOfDay();
        LocalDateTime endDateTime = endDate.plusDays(1).atStartOfDay();
        List<WorkoutLog> logs = workoutLogRepository.findByUserIdAndDateRange(
                userId, startDateTime, endDateTime
        );
        // Calculate statistics
        int totalWorkouts = (int) workouts.stream()
                .filter(w -> w.getStatus() == WorkoutStatus.COMPLETED)
                .count();
        int totalExercises = logs.size();
        BigDecimal totalVolume = logs.stream()
                .map(this::calculateVolume)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        Integer totalDuration = workouts.stream()
                .map(WorkoutPlan::getDurationMinutes)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
        // Group exercises by muscle group
        Map<String, Integer> muscleGroupDistribution = logs.stream()
                .filter(log -> log.getExercise() != null && log.getExercise().getMuscleGroup() != null)
                .collect(Collectors.groupingBy(
                        log -> log.getExercise().getMuscleGroup().getDisplayName(),
                        Collectors.collectingAndThen(Collectors.counting(), Long::intValue)
                ));
        // Most performed exercises
        Map<String, Long> exerciseFrequency = logs.stream()
                .filter(log -> log.getExercise() != null)
                .collect(Collectors.groupingBy(
                        log -> log.getExercise().getName(),
                        Collectors.counting()
                ));
        List<ExerciseFrequencyDto> topExercises = exerciseFrequency.entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .limit(5)
                .map(entry -> ExerciseFrequencyDto.builder()
                        .exerciseName(entry.getKey())
                        .frequency(entry.getValue().intValue())
                        .build())
                .collect(Collectors.toList());
        return ReportResponse.builder()
                .reportType(reportType)
                .startDate(startDate)
                .endDate(endDate)
                .totalWorkouts(totalWorkouts)
                .totalExercises(totalExercises)
                .totalVolume(totalVolume)
                .totalDuration(totalDuration)
                .muscleGroupDistribution(muscleGroupDistribution)
                .topExercises(topExercises)
                .generatedAt(LocalDateTime.now())
                .build();
    }

    private int calculateCurrentStreak(Long userId) {
        List<WorkoutPlan> completedWorkouts = workoutPlanRepository.findByUserIdAndStatus(
                userId, WorkoutStatus.COMPLETED
        );
        if (completedWorkouts.isEmpty()) {
            return 0;
        }
        // Sort by completed date
        completedWorkouts.sort((a, b) -> {
            LocalDate dateA = a.getCompletedAt() != null ? a.getCompletedAt().toLocalDate() : a.getScheduledDate();
            LocalDate dateB = b.getCompletedAt() != null ? b.getCompletedAt().toLocalDate() : b.getScheduledDate();
            return dateB.compareTo(dateA);
        });
        int streak = 0;
        LocalDate lastDate = LocalDate.now();
        for (WorkoutPlan workout : completedWorkouts) {
            LocalDate workoutDate = workout.getCompletedAt() != null
                    ? workout.getCompletedAt().toLocalDate()
                    : workout.getScheduledDate();
            if (workoutDate == null) continue;
            long daysBetween = ChronoUnit.DAYS.between(workoutDate, lastDate);
            if (daysBetween <= 1) {
                streak++;
                lastDate = workoutDate;
            } else {
                break;
            }
        }
        return streak;
    }

    private WeeklyAverageDto calculateWeeklyAverage(Long userId) {
        LocalDate fourWeeksAgo = LocalDate.now().minusWeeks(4);
        List<WorkoutPlan> recentWorkouts = workoutPlanRepository.findByUserIdWithFilters(
                userId, WorkoutStatus.COMPLETED, fourWeeksAgo, LocalDate.now(), null
        ).getContent();
        double avgWorkouts = recentWorkouts.size() / 4.0;
        Integer totalMinutes = recentWorkouts.stream()
                .map(WorkoutPlan::getDurationMinutes)
                .filter(Objects::nonNull)
                .reduce(0, Integer::sum);
        double avgMinutes = totalMinutes / 4.0;
        return WeeklyAverageDto.builder()
                .workoutsPerWeek(avgWorkouts)
                .minutesPerWeek(avgMinutes)
                .build();
    }

    private BigDecimal calculateVolume(WorkoutLog log) {
        if (log.getActualWeight() == null || log.getActualReps() == null || log.getActualSets() == null) {
            return BigDecimal.ZERO;
        }
        return log.getActualWeight()
                .multiply(BigDecimal.valueOf(log.getActualReps()))
                .multiply(BigDecimal.valueOf(log.getActualSets()));
    }

    private ExerciseProgressDto mapToExerciseProgressDto(UserProgress progress) {
        BigDecimal progressPercentage = BigDecimal.ZERO;
        if (progress.getMaxWeight() != null && progress.getMaxWeight().compareTo(BigDecimal.ZERO) > 0) {
            // Calculate progress (simplified - you might want to track initial weight)
            progressPercentage = BigDecimal.valueOf(33.3); // Placeholder
        }
        return ExerciseProgressDto.builder()
                .exerciseId(progress.getExercise().getId())
                .exerciseName(progress.getExercise().getName())
                .maxWeight(progress.getMaxWeight())
                .maxReps(progress.getMaxReps())
                .progressPercentage(progressPercentage)
                .build();
    }

}
