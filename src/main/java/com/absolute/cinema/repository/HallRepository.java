package com.absolute.cinema.repository;

import com.absolute.cinema.entity.Hall;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface HallRepository extends JpaRepository<Hall, UUID> {
}
