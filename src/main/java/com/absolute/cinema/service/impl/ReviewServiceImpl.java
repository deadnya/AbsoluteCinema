package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.ForbiddenException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.PageDTO;
import com.absolute.cinema.dto.review.ReviewCreateDTO;
import com.absolute.cinema.dto.review.ReviewDTO;
import com.absolute.cinema.dto.review.ReviewPagedListDTO;
import com.absolute.cinema.dto.review.ReviewUpdateDTO;
import com.absolute.cinema.entity.Film;
import com.absolute.cinema.entity.Review;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.mapper.ReviewMapper;
import com.absolute.cinema.repository.FilmRepository;
import com.absolute.cinema.repository.ReviewRepository;
import com.absolute.cinema.repository.UserRepository;
import com.absolute.cinema.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final FilmRepository filmRepository;
    private final UserRepository userRepository;
    private final ReviewMapper reviewMapper;

    private UUID currentUserId() {
        var email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewPagedListDTO getFilmReviews(UUID filmId, int page, int size) {
        if (!filmRepository.existsById(filmId))
            throw new NotFoundException("Film not found");
        if (page < 0) throw new BadRequestException("Page index is less than 0");
        if (size < 1) throw new BadRequestException("Page size is less than 1");

        var pageable = PageRequest.of(page, size);
        var pageResult = reviewRepository.findByFilm_Id(filmId, pageable);

        var data = pageResult.getContent().stream().map(reviewMapper::toDTO).toList();
        var pagination = new PageDTO(page, size, (int) pageResult.getTotalElements(), pageResult.getTotalPages());
        return new ReviewPagedListDTO(data, pagination);
    }

    @Override
    @Transactional(readOnly = true)
    public ReviewDTO getById(UUID id) {
        var review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));
        return reviewMapper.toDTO(review);
    }

    @Override
    public ReviewDTO create(UUID filmId, ReviewCreateDTO dto) {
        Film film = filmRepository.findById(filmId)
                .orElseThrow(() -> new NotFoundException("Film not found"));

        var userId = currentUserId();
        if (reviewRepository.existsByFilm_IdAndClient_Id(filmId, userId))
            throw new BadRequestException("You have already left a review for this film");

        var user = userRepository.findById(userId).orElseThrow(() -> new NotFoundException("User not found"));

        var review = new Review();
        review.setFilm(film);
        review.setClient(user);
        review.setRating(dto.rating());
        review.setText(dto.text());

        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    @Override
    public ReviewDTO update(UUID id, ReviewUpdateDTO dto) {
        var userId = currentUserId();
        var review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getClient().getId().equals(userId))
            throw new ForbiddenException("You can edit only your review");

        if (dto.rating() != null) review.setRating(dto.rating());
        if (dto.text() != null)   review.setText(dto.text());

        return reviewMapper.toDTO(reviewRepository.save(review));
    }

    @Override
    public void delete(UUID id) {
        var userId = currentUserId();
        var review = reviewRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Review not found"));

        if (!review.getClient().getId().equals(userId))
            throw new ForbiddenException("You can delete only your review");

        reviewRepository.delete(review);
    }
}
