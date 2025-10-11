package com.github.superz97.tracker.service.impl;

import com.github.superz97.tracker.dto.response.ExerciseResponse;
import com.github.superz97.tracker.entity.Exercise;
import com.github.superz97.tracker.entity.model.ExerciseCategory;
import com.github.superz97.tracker.entity.model.MuscleGroup;
import com.github.superz97.tracker.exception.ResourceNotFoundException;
import com.github.superz97.tracker.mapper.ExerciseMapper;
import com.github.superz97.tracker.repository.ExerciseRepository;
import com.github.superz97.tracker.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Override
    @Transactional(readOnly = true)
    public ExerciseResponse getExerciseById(Long id) {
        Exercise exercise = exerciseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Exercise not found with id: " + id));
        return exerciseMapper.toExerciseResponse(exercise);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExerciseResponse> getAllExercises(ExerciseCategory category, MuscleGroup muscleGroup, String searchTerm, Pageable pageable) {
        Page<Exercise> exercises = exerciseRepository.findWithFilters(category, muscleGroup, searchTerm, pageable);
        return exercises.map(exerciseMapper::toExerciseResponse);
    }

    @Override
    public List<String> getAllCategories() {
        return Arrays.stream(ExerciseCategory.values())
                .map(ExerciseCategory::name)
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllMuscleGroups() {
        return Arrays.stream(MuscleGroup.values())
                .map(MuscleGroup::name)
                .collect(Collectors.toList());
    }
}
