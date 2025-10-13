package com.github.superz97.tracker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgressResponse {

    private String message;
    private Integer totalWorkouts;
    private Integer currentStreak;
    private BigDecimal totalVolume;
    private List<ExerciseProgressDto> favoriteExercises;
    private WeeklyAverageDto weeklyAverage;

    // For individual exercise progress
    private String exerciseName;
    private BigDecimal maxWeight;
    private Integer maxReps;
    private LocalDateTime lastPerformed;
    private List<ProgressDataPoint> progressHistory;

}
