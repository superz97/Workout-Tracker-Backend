package com.github.superz97.tracker.mapper;

import com.github.superz97.tracker.dto.response.WorkoutExerciseResponse;
import com.github.superz97.tracker.dto.response.WorkoutResponse;
import com.github.superz97.tracker.entity.WorkoutExercise;
import com.github.superz97.tracker.entity.WorkoutPlan;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface WorkoutMapper {

    @Mapping(target = "exercises", source = "exercises")
    WorkoutResponse toWorkoutResponse(WorkoutPlan workoutPlan);

    @Mapping(target = "exerciseId", source = "exercise.id")
    @Mapping(target = "exerciseName", source = "exercise.name")
    @Mapping(target = "exerciseCategory", source = "exercise.category")
    @Mapping(target = "muscleGroup", source = "exercise.muscleGroup")
    WorkoutExerciseResponse toWorkoutExerciseResponse(WorkoutExercise workoutExercise);

    List<WorkoutExerciseResponse> toWorkoutExerciseResponses(List<WorkoutExercise> exercises);

}
