package ru.practicum.shareit.user.service;


import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;

import java.util.List;

public interface UserService {
    UserResponse create(UserRequest userRequest);

    UserResponse update(Long userId, UserRequest userRequest);

    void delete(Long userId);

    UserResponse getUserById(Long userId);

    List<UserResponse> findAll();
}