package com.github.superz97.tracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutLogResponse {

    private Long id;
    private Long workoutPlanId;
    private String workoutPlanName;
    private Long exerciseId;
    private String exerciseName;
    private String exerciseCategory;
    private String muscleGroup;
    private Integer actualSets;
    private Integer actualReps;
    private BigDecimal actualWeight;
    private BigDecimal volume;
    private LocalDateTime loggedAt;
    private String notes;

}
