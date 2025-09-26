package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.*;
import com.absolute.cinema.entity.SeatCategory;
import com.absolute.cinema.mapper.SeatCategoryMapper;
import com.absolute.cinema.repository.SeatCategoryRepository;
import com.absolute.cinema.service.SeatCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SeatCategoryServiceImpl implements SeatCategoryService {

    private final SeatCategoryRepository seatCategoryRepository;
    private final SeatCategoryMapper seatCategoryMapper;

    @Override
    public SeatCategoryPagedListDTO getSeatCategories(int page, int size) {

        if (page < 0) throw new BadRequestException("Page index is less than 0");
        if (size < 1) throw new BadRequestException("Page size is less than 1");

        var pageable = PageRequest.of(page, size);

        var seatCategoriesPage = seatCategoryRepository.findAll(pageable);

        var seatCategoryDTOs = seatCategoriesPage.getContent().stream()
                .map(seatCategoryMapper::toDTO)
                .toList();

        var pageDTO = new PageDTO(
                page,
                size,
                (int) seatCategoriesPage.getTotalElements(),
                seatCategoriesPage.getTotalPages()
        );

        return new SeatCategoryPagedListDTO(seatCategoryDTOs, pageDTO);
    }

    @Override
    public SeatCategoryDTO createSeatCategory(CreateSeatCategoryDTO createSeatCategoryDTO) {
        SeatCategory seatCategory = seatCategoryMapper.toSeatCategory(createSeatCategoryDTO);
        return seatCategoryMapper.toDTO(seatCategoryRepository.save(seatCategory));
    }

    @Override
    public SeatCategoryDTO getSeatCategoryById(UUID id) {

        SeatCategory seatCategory = seatCategoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Seat category with id: %s not found", id))
        );

        return seatCategoryMapper.toDTO(seatCategory);
    }

    @Override
    public SeatCategoryDTO editSeatCategory(UUID id, EditSeatCategoryDTO editSeatCategoryDTO) {

        SeatCategory seatCategory = seatCategoryRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Seat category with id: %s not found", id))
        );

        seatCategory.setName(editSeatCategoryDTO.name());
        seatCategory.setPriceCents(editSeatCategoryDTO.priceCents());

        return seatCategoryMapper.toDTO(seatCategoryRepository.save(seatCategory));
    }

    @Override
    public void deleteSeatCategory(UUID id) {

        if (!seatCategoryRepository.existsById(id)) {
            throw new NotFoundException(String.format("Seat category with id: %s not found", id));
        }

        seatCategoryRepository.deleteById(id);
    }
}
