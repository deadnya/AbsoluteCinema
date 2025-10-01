package com.absolute.cinema.repository;

import com.absolute.cinema.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {
    Page<Review> findByFilm_Id(UUID filmId, Pageable pageable);
    boolean existsByFilm_IdAndClient_Id(UUID filmId, UUID clientId);
}
