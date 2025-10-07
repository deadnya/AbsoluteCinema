package com.absolute.cinema.service;

import com.absolute.cinema.dto.hall.*;

import java.util.List;
import java.util.UUID;

public interface HallService {
    HallPagedListDTO getAll(int page, int size);
    HallDTO getById(UUID id);
    HallDTO create(HallCreateRequestDTO req);
    HallDTO update(UUID id, HallUpdateRequestDTO req);
    void delete(UUID id);
}
