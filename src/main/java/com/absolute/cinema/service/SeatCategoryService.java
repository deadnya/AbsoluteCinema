package com.absolute.cinema.service;

import com.absolute.cinema.dto.CreateSeatCategoryDTO;
import com.absolute.cinema.dto.EditSeatCategoryDTO;
import com.absolute.cinema.dto.SeatCategoryDTO;
import com.absolute.cinema.dto.SeatCategoryPagedListDTO;

import java.util.UUID;

public interface SeatCategoryService {
    SeatCategoryPagedListDTO getSeatCategories(int page, int size);
    SeatCategoryDTO createSeatCategory(CreateSeatCategoryDTO createSeatCategoryDTO);
    SeatCategoryDTO getSeatCategoryById(UUID id);
    SeatCategoryDTO editSeatCategory(UUID id, EditSeatCategoryDTO editSeatCategoryDTO);
    void deleteSeatCategory(UUID id);
}
