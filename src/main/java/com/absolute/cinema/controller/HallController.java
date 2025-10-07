package com.absolute.cinema.controller;

import com.absolute.cinema.dto.hall.*;
import com.absolute.cinema.service.HallPlanService;
import com.absolute.cinema.service.HallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.UUID;

@RestController
@RequestMapping("/halls")
@RequiredArgsConstructor
@CrossOrigin
public class HallController {

    private final HallService hallService;
    private final HallPlanService hallPlanService;

    @GetMapping
    public HallPagedListDTO list(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size
    ) {
        return hallService.getAll(page, size);
    }

    @GetMapping("/{id}")
    public HallDTO get(@PathVariable UUID id) {
        return hallService.getById(id);
    }

    @PostMapping
    public ResponseEntity<HallDTO> create(@Valid @RequestBody HallCreateRequestDTO req) {
        var dto = hallService.create(req);
        return ResponseEntity.created(URI.create("/halls/" + dto.id())).body(dto);
    }

    @PutMapping("/{id}")
    public HallDTO update(@PathVariable UUID id, @Valid @RequestBody HallUpdateRequestDTO req) {
        return hallService.update(id, req);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        hallService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/plan")
    public HallPlanDTO getPlan(@PathVariable UUID id) {
        return hallPlanService.getPlan(id);
    }

    @PutMapping("/{id}/plan")
    public HallPlanDTO updatePlan(@PathVariable UUID id,
                                  @Valid @RequestBody HallPlanUpdateRequestDTO req) {
        return hallPlanService.updatePlan(id, req);
    }
}
