package com.absolute.cinema.controller;

import com.absolute.cinema.dto.review.ReviewCreateDTO;
import com.absolute.cinema.dto.review.ReviewDTO;
import com.absolute.cinema.dto.review.ReviewPagedListDTO;
import com.absolute.cinema.dto.review.ReviewUpdateDTO;
import com.absolute.cinema.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class ReviewController {
    private final ReviewService reviewService;

    @GetMapping("/films/{filmId}/reviews")
    public ReviewPagedListDTO getFilmReviews(
            @PathVariable UUID filmId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(name = "size", defaultValue = "20") int size
    ) {
        return reviewService.getFilmReviews(filmId, page, size);
    }

    @PostMapping("/films/{filmId}/reviews")
    public ReviewDTO create(@PathVariable UUID filmId, @Valid @RequestBody ReviewCreateDTO dto) {
        return reviewService.create(filmId, dto);
    }

    @GetMapping("/reviews/{id}")
    public ReviewDTO getById(@PathVariable UUID id) {
        return reviewService.getById(id);
    }

    @PutMapping("/reviews/{id}")
    public ReviewDTO update(@PathVariable UUID id, @Valid @RequestBody ReviewUpdateDTO dto) {
        return reviewService.update(id, dto);
    }

    @DeleteMapping("/reviews/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        reviewService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
