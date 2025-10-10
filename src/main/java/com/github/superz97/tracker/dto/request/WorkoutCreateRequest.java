package com.github.superz97.tracker.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutCreateRequest {

    @NotBlank(message = "Workout name is required")
    @Size(max = 100)
    private String name;

    private LocalDate scheduledDate;

    private LocalTime scheduledTime;

    @NotNull(message = "Exercises list is required")
    @Size(min = 1, message = "At least one exercise is required")
    private List<WorkoutExerciseDto> exercises;

    private String notes;

}
