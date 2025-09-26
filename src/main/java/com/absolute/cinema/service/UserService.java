package com.absolute.cinema.service;

import com.absolute.cinema.dto.UpdateUserDTO;
import com.absolute.cinema.dto.UserDTO;

import java.util.UUID;

public interface UserService {
    UserDTO getUserById(UUID id);
    UserDTO updateUser(UUID id, UpdateUserDTO updateUserDTO);
}
