package com.github.superz97.tracker.repository;

import com.github.superz97.tracker.entity.Exercise;
import com.github.superz97.tracker.entity.model.ExerciseCategory;
import com.github.superz97.tracker.entity.model.MuscleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    List<Exercise> findByCategory(ExerciseCategory category);
    List<Exercise> findByMuscleGroup(MuscleGroup muscleGroup);

//    @Query("""
//    SELECT e FROM Exercise e WHERE (:category IS NULL OR e.category = :category)
//    AND (:muscleGroup IS NULL OR e.muscleGroup = :muscleGroup)
//    AND (:searchTerm IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')))
//""")
    @Query("""
        SELECT e FROM Exercise e WHERE (:category IS NULL OR e.category = :category)
        AND (:muscleGroup IS NULL OR e.muscleGroup = :muscleGroup)
        AND (:searchTerm IS NULL OR LOWER(e.name) LIKE :searchTerm)
    """)
    Page<Exercise> findWithFilters(
            @Param("category") ExerciseCategory category,
            @Param("muscleGroup") MuscleGroup muscleGroup,
            @Param("searchTerm") String searchTerm,
            Pageable pageable
    );

}
