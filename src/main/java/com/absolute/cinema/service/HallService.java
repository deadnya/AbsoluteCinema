package com.absolute.cinema.service;

import com.absolute.cinema.dto.hall.HallCreateRequestDTO;
import com.absolute.cinema.dto.hall.HallDTO;
import com.absolute.cinema.dto.hall.HallListItemDTO;
import com.absolute.cinema.dto.hall.HallUpdateRequestDTO;

import java.util.List;
import java.util.UUID;

public interface HallService {
    List<HallListItemDTO> getAll();
    HallDTO getById(UUID id);
    HallDTO create(HallCreateRequestDTO req);
    HallDTO update(UUID id, HallUpdateRequestDTO req);
    void delete(UUID id);
}
