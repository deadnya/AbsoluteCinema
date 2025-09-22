package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.common.exception.custom.UnauthorizedException;
import com.absolute.cinema.common.security.JwtTokenService;
import com.absolute.cinema.dto.AuthResponseDTO;
import com.absolute.cinema.dto.LoginRequestDTO;
import com.absolute.cinema.dto.RegisterRequestDTO;
import com.absolute.cinema.entity.Role;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.repository.RoleRepository;
import com.absolute.cinema.repository.UserRepository;
import com.absolute.cinema.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenService jwtTokenService;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthResponseDTO login(LoginRequestDTO request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.email(),
                            request.password()
                    )
            );

            User user = userRepository.findByEmail(request.email())
                    .orElseThrow(() -> new UnauthorizedException("Invalid credentials"));

            String accessToken = jwtTokenService.generateToken(user);

            jwtTokenService.saveToken(user, accessToken);

            return new AuthResponseDTO(accessToken);
        } catch (Exception e) {
            throw new UnauthorizedException("Invalid credentials");
        }
    }

    @Override
    public void logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication != null && authentication.getPrincipal() instanceof User user) {
            jwtTokenService.revokeAllTokens(user);
        }

        SecurityContextHolder.clearContext();
    }

    @Override
    @Transactional
    public AuthResponseDTO register(RegisterRequestDTO request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new BadRequestException("User with email " + request.email() + " already exists");
        }

        Role userRole = roleRepository.findByRole(Role.RoleType.USER)
                .orElseThrow(() -> new NotFoundException("Default USER role not found"));

        User user = new User();
        user.setEmail(request.email());
        user.setPassword(passwordEncoder.encode(request.password()));
        user.setFirstName(request.firstName());
        user.setLastName(request.lastName());
        user.setAge(request.age());
        user.setGender(request.gender());
        user.setRoles(Set.of(userRole));

        User savedUser = userRepository.save(user);

        String accessToken = jwtTokenService.generateToken(savedUser);

        jwtTokenService.saveToken(savedUser, accessToken);

        return new AuthResponseDTO(accessToken);
    }
}
