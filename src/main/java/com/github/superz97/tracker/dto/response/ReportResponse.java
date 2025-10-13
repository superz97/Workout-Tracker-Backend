package com.github.superz97.tracker.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ReportResponse {

    private String reportType;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer totalWorkouts;
    private Integer totalExercises;
    private BigDecimal totalVolume;
    private Integer totalDuration;
    private Map<String, Integer> muscleGroupDistribution;
    private List<ExerciseFrequencyDto> topExercises;
    private LocalDateTime generatedAt;

}
