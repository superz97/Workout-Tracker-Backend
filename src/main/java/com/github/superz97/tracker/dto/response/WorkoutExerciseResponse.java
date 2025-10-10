package com.github.superz97.tracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseResponse {

    private Long id;
    private Long exerciseId;
    private String exerciseName;
    private String exerciseCategory;
    private String muscleGroup;
    private Integer sets;
    private Integer reps;
    private BigDecimal weight;
    private Integer restSeconds;
    private Integer orderIndex;
    private String notes;

}
