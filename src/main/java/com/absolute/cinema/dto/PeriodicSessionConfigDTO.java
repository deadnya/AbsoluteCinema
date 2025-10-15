package com.absolute.cinema.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Date;

public record PeriodicSessionConfigDTO(
        @NotNull(message = "Period cannot be null")
        SessionCreatePeriod period,
        
        @NotNull(message = "Period generation end date cannot be null")
        Date periodGenerationEndsAt
) {
}