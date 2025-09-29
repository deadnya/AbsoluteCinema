package com.absolute.cinema.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;

public record CreateSessionDTO(

        @NotNull(message = "Film ID cannot be null")
        UUID filmId,

        @NotNull(message = "Hall ID cannot be null")
        UUID hallId,

        @NotNull(message = "Start time cannot be null")
        Date startAt
) { }