package com.github.superz97.tracker.repository;

import com.github.superz97.tracker.entity.WorkoutExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkoutExerciseRepository extends JpaRepository<WorkoutExercise, Long> {

    List<WorkoutExercise> findByWorkoutPlanIdOrderByOrderIndex(Long workoutPlanId);
    Optional<WorkoutExercise> findByIdAndWorkoutPlanId(Long id, Long workoutPlanId);

    @Modifying
    @Query("UPDATE WorkoutExercise we SET we.orderIndex = :orderIndex WHERE we.id = :id")
    void updateOrderIndex(@Param("id") Long id, @Param("orderIndex") Integer orderIndex);

}
