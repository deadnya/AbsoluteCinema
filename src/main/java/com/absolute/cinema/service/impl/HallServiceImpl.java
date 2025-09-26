package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.hall.HallCreateRequestDTO;
import com.absolute.cinema.dto.hall.HallDTO;
import com.absolute.cinema.dto.hall.HallListItemDTO;
import com.absolute.cinema.dto.hall.HallUpdateRequestDTO;
import com.absolute.cinema.entity.Hall;
import com.absolute.cinema.mapper.HallMapper;
import com.absolute.cinema.repository.HallRepository;
import com.absolute.cinema.service.HallService;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class HallServiceImpl implements HallService {

    private final HallRepository hallRepository;
    private final HallMapper hallMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HallListItemDTO> getAll() {
        return hallRepository.findAll(Sort.by("number").ascending())
                .stream().map(hallMapper::toListItem).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public HallDTO getById(UUID id) {
        var hall = hallRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hall not found"));
        return hallMapper.toDTO(hall);
    }

    @Override
    public HallDTO create(HallCreateRequestDTO req) {
        Hall hall = hallMapper.fromCreate(req);
        hall = hallRepository.save(hall);
        return hallMapper.toDTO(hall);
    }

    @Override
    public HallDTO update(UUID id, HallUpdateRequestDTO req) {
        var hall = hallRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Hall not found"));
        hallMapper.updateEntity(hall, req);
        return hallMapper.toDTO(hall);
    }

    @Override
    public void delete(UUID id) {
        if (!hallRepository.existsById(id)) {
            throw new NotFoundException("Hall not found");
        }
        hallRepository.deleteById(id);
    }
}
