package com.github.superz97.tracker.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseDto {

    @NotNull(message = "Exercise ID is required")
    private Long exerciseId;

    @NotNull(message = "Sets is required")
    @Min(value = 1, message = "Sets must be at least 1")
    private Integer sets;

    @NotNull(message = "Reps is required")
    @Min(value = 1, message = "Reps must be at least 1")
    private Integer reps;

    @Min(value = 0, message = "Weight cannot be negative")
    private BigDecimal weight;

    @Min(value = 0, message = "Rest seconds cannot be negative")
    private Integer restSeconds;

    private Integer orderIndex;

    private String notes;

}
