package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.SeatCategoryDTO;
import com.absolute.cinema.dto.hall.HallPlanDTO;
import com.absolute.cinema.dto.hall.HallPlanUpdateRequestDTO;
import com.absolute.cinema.dto.hall.SeatInPlanDTO;
import com.absolute.cinema.dto.hall.SeatUpsertDTO;
import com.absolute.cinema.entity.Hall;
import com.absolute.cinema.entity.Seat;
import com.absolute.cinema.repository.HallRepository;
import com.absolute.cinema.repository.SeatCategoryRepository;
import com.absolute.cinema.repository.SeatRepository;
import com.absolute.cinema.service.HallPlanService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.LinkedHashMap;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class HallPlanServiceImpl implements HallPlanService {

    private final HallRepository hallRepository;
    private final SeatRepository seatRepository;
    private final SeatCategoryRepository seatCategoryRepository;

    @Override
    public HallPlanDTO getPlan(UUID hallId) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException("Hall not found"));

        var seats = seatRepository.findByHallId(hall.getId());
        int rows = seats.stream().mapToInt(Seat::getRow).max().orElse(0);

        var categories = seats.stream()
                .map(Seat::getCategory)
                .collect(Collectors.toMap(
                        c -> c.getId(),
                        c -> new SeatCategoryDTO(c.getId(), c.getName(), c.getPriceCents()),
                        (a, b) -> a,
                        LinkedHashMap::new
                ))
                .values().stream().toList();

        var seatDtos = seats.stream()
                .map(s -> new SeatInPlanDTO(s.getId(), s.getRow(), s.getNumber(), s.getCategory().getId(), null))
                .toList();

        return new HallPlanDTO(hall.getId(), rows, seatDtos, categories);
    }

    @Override
    @Transactional
    public HallPlanDTO updatePlan(UUID hallId, HallPlanUpdateRequestDTO request) {
        Hall hall = hallRepository.findById(hallId)
                .orElseThrow(() -> new NotFoundException("Hall not found"));

        if (request.seats().isEmpty()) {
            throw new BadRequestException("Seats list cannot be empty");
        }

        int maxRowInSeats = request.seats().stream().mapToInt(SeatUpsertDTO::row).max().orElse(0);
        if (request.rows() < maxRowInSeats) {
            throw new BadRequestException("Rows is less than max seat row (" + maxRowInSeats + ")");
        }

        var requestedCategoryIds = request.seats().stream()
                .map(SeatUpsertDTO::categoryId)
                .collect(Collectors.toSet());

        var existingCategories = seatCategoryRepository.findAllById(requestedCategoryIds)
                .stream().map(c -> c.getId()).collect(Collectors.toSet());

        if (!existingCategories.containsAll(requestedCategoryIds)) {
            var missing = new HashSet<>(requestedCategoryIds);
            missing.removeAll(existingCategories);
            throw new NotFoundException("Seat categories not found: " + missing);
        }

        seatRepository.deleteByHallId(hall.getId());

        List<Seat> toSave = new ArrayList<>(request.seats().size());
        var categoriesById = seatCategoryRepository.findAllById(requestedCategoryIds)
                .stream().collect(Collectors.toMap(c -> c.getId(), c -> c));

        for (SeatUpsertDTO s : request.seats()) {
            var entity = new Seat();
            entity.setHall(hall);
            entity.setRow(s.row());
            entity.setNumber(s.number());
            entity.setCategory(categoriesById.get(s.categoryId()));
            toSave.add(entity);
        }

        seatRepository.saveAll(toSave);

        return getPlan(hall.getId());
    }

}
