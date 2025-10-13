package com.github.superz97.tracker.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProgressDataPoint {

    private LocalDate date;
    private BigDecimal weight;
    private Integer reps;
    private Integer sets;
    private BigDecimal volume;

}
