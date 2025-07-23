package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.dto.UserUpdateRequest;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest userRequest);

    UserResponse update(Long userId, UserUpdateRequest userUpdateRequest);

    void delete(Long userId);

    UserResponse getUserById(Long userId);

    List<UserResponse> findAll();
}