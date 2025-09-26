package com.absolute.cinema.service.impl;

import com.absolute.cinema.common.exception.custom.BadRequestException;
import com.absolute.cinema.common.exception.custom.NotFoundException;
import com.absolute.cinema.dto.UpdateUserDTO;
import com.absolute.cinema.dto.UserDTO;
import com.absolute.cinema.entity.User;
import com.absolute.cinema.mapper.UserMapper;
import com.absolute.cinema.repository.UserRepository;
import com.absolute.cinema.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserDTO getUserById(UUID id) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("User with id: %s not found", id))
        );

        return userMapper.toDTO(user);
    }

    @Override
    public UserDTO updateUser(UUID id, UpdateUserDTO updateUserDTO) {

        User user = userRepository.findById(id).orElseThrow(
                () -> new NotFoundException(String.format("User with id: %s not found", id))
        );

        if (userRepository.existsByEmail(updateUserDTO.email()) && !user.getEmail().equals(updateUserDTO.email())) {
            throw new BadRequestException("Email is already in use");
        }

        user.setEmail(updateUserDTO.email());
        user.setFirstName(updateUserDTO.firstName());
        user.setLastName(updateUserDTO.lastName());
        user.setAge(updateUserDTO.age());
        user.setGender(updateUserDTO.gender());

        return userMapper.toDTO(userRepository.save(user));
    }
}
