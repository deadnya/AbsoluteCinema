package com.absolute.cinema.repository;

import com.absolute.cinema.entity.SeatCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SeatCategoryRepository extends JpaRepository<SeatCategory, UUID> {
}
