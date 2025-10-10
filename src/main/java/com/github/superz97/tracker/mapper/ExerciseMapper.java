package com.github.superz97.tracker.mapper;

import com.github.superz97.tracker.dto.response.ExerciseResponse;
import com.github.superz97.tracker.entity.Exercise;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    ExerciseResponse toExerciseResponse(Exercise exercise);
    List<ExerciseResponse> toExerciseResponses(List<Exercise> exercises);

}
