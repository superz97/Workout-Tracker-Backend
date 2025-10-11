package com.github.superz97.tracker.service;

import com.github.superz97.tracker.dto.response.ExerciseResponse;
import com.github.superz97.tracker.entity.model.ExerciseCategory;
import com.github.superz97.tracker.entity.model.MuscleGroup;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ExerciseService {

    ExerciseResponse getExerciseById(Long id);
    Page<ExerciseResponse> getAllExercises(ExerciseCategory category, MuscleGroup muscleGroup, String searchTerm, Pageable pageable);
    List<String> getAllCategories();
    List<String> getAllMuscleGroups();

}
