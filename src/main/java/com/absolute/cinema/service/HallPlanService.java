package com.absolute.cinema.service;

import com.absolute.cinema.dto.hall.HallPlanDTO;
import com.absolute.cinema.dto.hall.HallPlanUpdateRequestDTO;

import java.util.UUID;

public interface HallPlanService {
    HallPlanDTO getPlan(UUID hallId);
    HallPlanDTO updatePlan(UUID hallId, HallPlanUpdateRequestDTO req);
}
