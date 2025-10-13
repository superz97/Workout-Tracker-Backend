package com.github.superz97.tracker.repository;

import com.github.superz97.tracker.entity.WorkoutPlan;
import com.github.superz97.tracker.entity.model.WorkoutStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutPlanRepository extends JpaRepository<WorkoutPlan, Long> {

    Page<WorkoutPlan> findByUserId(Long userId, Pageable pageable);
    Optional<WorkoutPlan> findByIdAndUserId(Long id, Long userId);
    List<WorkoutPlan> findByUserIdAndStatus(Long userId, WorkoutStatus status);

//    @Query("""
//    SELECT w FROM WorkoutPlan w WHERE w.user.id = :userId
//    AND (:status IS NULL OR w.status = :status)
//    AND (:startDate IS NULL OR w.scheduledDate >= :startDate)
//    AND (:endDate IS NULL OR w.scheduledDate <= :endDate)
//""")
    @Query("""
    SELECT w FROM WorkoutPlan w WHERE w.user.id = :userId
    AND (:status IS NULL OR w.status = :status)
    AND (w.scheduledDate >= COALESCE(:startDate, w.scheduledDate))
    AND (w.scheduledDate <= COALESCE(:endDate, w.scheduledDate))
    """)
    Page<WorkoutPlan> findByUserIdWithFilters(
            @Param("userId") Long userId,
            @Param("status") WorkoutStatus status,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate,
            Pageable pageable
            );

    @Query("""
    SELECT w FROM WorkoutPlan w WHERE w.user.id = :userId
    AND w.scheduledDate = :date ORDER BY w.scheduledTime
""")
    List<WorkoutPlan> findByUserIdAndScheduledDate(
            @Param("userId") Long userId,
            @Param("date") LocalDate date
    );

}
