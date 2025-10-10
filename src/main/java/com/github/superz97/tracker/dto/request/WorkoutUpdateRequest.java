package com.github.superz97.tracker.dto.request;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutUpdateRequest {

    @Size(max = 100)
    private String name;

    private String description;

    private LocalDate scheduledDate;

    private LocalTime scheduledTime;

    private String status;

    private Integer durationMinutes;

    private String notes;

}
