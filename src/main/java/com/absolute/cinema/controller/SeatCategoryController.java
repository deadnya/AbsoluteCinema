package com.absolute.cinema.controller;

import com.absolute.cinema.dto.CreateSeatCategoryDTO;
import com.absolute.cinema.dto.EditSeatCategoryDTO;
import com.absolute.cinema.dto.SeatCategoryDTO;
import com.absolute.cinema.dto.SeatCategoryPagedListDTO;
import com.absolute.cinema.service.SeatCategoryService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/seat-categories")
@RequiredArgsConstructor
@CrossOrigin
public class SeatCategoryController {

    private final SeatCategoryService seatCategoryService;

    @GetMapping
    public ResponseEntity<SeatCategoryPagedListDTO> getAllSeatCategories(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok(seatCategoryService.getSeatCategories(page, size));
    }

    @PostMapping
    public ResponseEntity<SeatCategoryDTO> createSeatCategory(
            @RequestBody @Valid CreateSeatCategoryDTO createSeatCategoryDTO
    ) {
        return ResponseEntity.ok(seatCategoryService.createSeatCategory(createSeatCategoryDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SeatCategoryDTO> getSeatCategoryById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(seatCategoryService.getSeatCategoryById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<SeatCategoryDTO> editSeatCategory(
            @PathVariable UUID id, @Valid @RequestBody EditSeatCategoryDTO editSeatCategoryDTO
    ) {
        return ResponseEntity.ok(seatCategoryService.editSeatCategory(id, editSeatCategoryDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSeatCategory(
            @PathVariable UUID id
    ) {
        seatCategoryService.deleteSeatCategory(id);
        return ResponseEntity.ok().build();
    }
}
