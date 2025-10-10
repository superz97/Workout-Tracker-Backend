package com.github.superz97.tracker.repository;

import com.github.superz97.tracker.entity.WorkoutLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WorkoutLogRepository extends JpaRepository<WorkoutLog, Long> {

    Page<WorkoutLog> findByUserId(Long userId, Pageable pageable);
    List<WorkoutLog> findByUserIdAndWorkoutPlanId(Long userId, Long workoutPlanId);

    @Query("""
    SELECT wl FROM WorkoutLog wl WHERE wl.user.id = :userId
    AND wl.loggedAt BETWEEN :startDate and :endDate
""")
    List<WorkoutLog> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
            );

    @Query("""
    SELECT wl FROM WorkoutLog wl WHERE wl.user.id = :userId
    AND wl.exercise.id = :exerciseId ORDER BY wl.loggedAt DESC
""")
    List<WorkoutLog> findByUserIdAndExerciseId(
            @Param("userId") Long userId,
            @Param("exerciseId") Long exerciseId
    );

}
