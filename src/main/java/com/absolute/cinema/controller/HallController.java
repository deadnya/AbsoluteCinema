package com.absolute.cinema.controller;

import com.absolute.cinema.dto.hall.*;
import com.absolute.cinema.service.HallPlanService;
import com.absolute.cinema.service.HallService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/halls")
@RequiredArgsConstructor
@Validated
public class HallController {

    private final HallService hallService;
    private final HallPlanService hallPlanService;

    @GetMapping
    public List<HallListItemDTO> list() {
        return hallService.getAll();
    }

    @GetMapping("/{id}")
    public HallDTO get(@PathVariable UUID id) {
        return hallService.getById(id);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<HallDTO> create(@Valid @RequestBody HallCreateRequestDTO req) {
        var dto = hallService.create(req);
        return ResponseEntity.created(URI.create("/halls/" + dto.id())).body(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public HallDTO update(@PathVariable UUID id, @Valid @RequestBody HallUpdateRequestDTO req) {
        return hallService.update(id, req);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        hallService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/plan")
    public HallPlanDTO getPlan(@PathVariable UUID id) {
        return hallPlanService.getPlan(id);
    }

    @PutMapping("/{id}/plan")
    @PreAuthorize("hasAuthority('ADMIN')")
    public HallPlanDTO updatePlan(@PathVariable UUID id,
                                  @Valid @RequestBody HallPlanUpdateRequestDTO req) {
        return hallPlanService.updatePlan(id, req);
    }
}
