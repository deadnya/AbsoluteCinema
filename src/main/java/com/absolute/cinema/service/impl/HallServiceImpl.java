package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.PageDTO;
import com.absolute.cinema.dto.hall.*;
import com.absolute.cinema.mapper.HallMapper;
import com.absolute.cinema.repository.HallRepository;
import com.absolute.cinema.service.HallPlanService;
import com.absolute.cinema.service.HallService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;
    private final HallPlanService hallPlanService;

    @Override
    @Transactional(readOnly = true)
    public HallPagedListDTO getAll(int page, int size) {
        if (page < 0) throw new BadRequestException("Page is less than 0");
        if (size <= 0) throw new BadRequestException("Size is less than or equal to 0");

        Pageable pageable = PageRequest.of(page, size);

        var hallsPage = hallRepository.findAll(pageable);

        var hallDTOs = hallsPage.getContent().stream()
                .map(hallMapper::toDTO)
                .toList();

        var pageDTO = new PageDTO(
                page,
                size,
                (int) hallsPage.getTotalElements(),
                hallsPage.getTotalPages()
        );

        return new HallPagedListDTO(hallDTOs, pageDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public HallDTO getById(UUID id) {
        var hall = hallRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Hall with id: %s not found", id)));
        return hallMapper.toDTO(hall);
    }

    @Override
    public HallDTO create(HallCreateRequestDTO req) {
        var hall = hallRepository.save(hallMapper.fromCreate(req));

        var planUpdateRequest = new HallPlanUpdateRequestDTO(req.rows(), req.seats());
        hallPlanService.updatePlan(hall.getId(), planUpdateRequest);
        
        return hallMapper.toDTO(hall);
    }

    @Override
    public HallDTO update(UUID id, HallUpdateRequestDTO req) {
        var hall = hallRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("Hall with id: %s not found", id)));
        hallMapper.updateEntity(hall, req);
        return hallMapper.toDTO(hall);
    }

    @Override
    public void delete(UUID id) {
        if (!hallRepository.existsById(id)) {
            throw new NotFoundException(String.format("Hall with id: %s not found", id));
        }
        hallRepository.deleteById(id);
    }
}
