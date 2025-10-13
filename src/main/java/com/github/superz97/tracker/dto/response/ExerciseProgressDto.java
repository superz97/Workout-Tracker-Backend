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
public class ExerciseProgressDto {

    private Long exerciseId;
    private String exerciseName;
    private Integer timesPerformed;
    private BigDecimal maxWeight;
    private Integer maxReps;
    private BigDecimal progressPercentage;

}
