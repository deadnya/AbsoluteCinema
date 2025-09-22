package com.absolute.cinema.service;

import com.absolute.cinema.dto.AuthResponseDTO;
import com.absolute.cinema.dto.LoginRequestDTO;
import com.absolute.cinema.dto.RegisterRequestDTO;

public interface AuthService {
    AuthResponseDTO login(LoginRequestDTO request);
    void logout();
    AuthResponseDTO register(RegisterRequestDTO request);
}
