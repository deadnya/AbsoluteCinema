package com.absolute.cinema.service;

import com.absolute.cinema.dto.review.ReviewCreateDTO;
import com.absolute.cinema.dto.review.ReviewDTO;
import com.absolute.cinema.dto.review.ReviewPagedListDTO;
import com.absolute.cinema.dto.review.ReviewUpdateDTO;

import java.util.UUID;

public interface ReviewService {
    ReviewPagedListDTO getFilmReviews(UUID filmId, int page, int size);
    ReviewDTO getById(UUID id);
    ReviewDTO create(UUID filmId, ReviewCreateDTO dto);
    ReviewDTO update(UUID id, ReviewUpdateDTO dto);
    void delete(UUID id);
}
