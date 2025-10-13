package com.github.superz97.tracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WeeklyAverageDto {

    private Double workoutsPerWeek;
    private Double minutesPerWeek;

}
