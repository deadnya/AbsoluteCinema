package com.absolute.cinema.controller;

import com.absolute.cinema.dto.UpdateUserDTO;
import com.absolute.cinema.dto.UserDTO;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
@CrossOrigin
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<UserDTO> getCurrentUser(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userService.getUserById(user.getId()));
    }

    @PutMapping("/me")
    public ResponseEntity<UserDTO> updateCurrentUser(
            @RequestBody @Valid UpdateUserDTO updateUserDTO,
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(userService.updateUser(user.getId(), updateUserDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getUserById(
            @PathVariable UUID id
    ) {
        return ResponseEntity.ok(userService.getUserById(id));
    }
}
