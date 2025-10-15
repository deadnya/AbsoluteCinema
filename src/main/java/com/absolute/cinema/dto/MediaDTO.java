package com.absolute.cinema.dto;

import com.absolute.cinema.entity.Media;

import java.util.Date;
import java.util.UUID;

public record MediaDTO(
        UUID id,
        String filename,
        String contentType,
        Media.MediaType mediaType,
        Date createdAt,
        Date updatedAt
) {
}