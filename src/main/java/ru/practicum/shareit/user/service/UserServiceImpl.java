package ru.practicum.shareit.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.dto.UserRequest;
import ru.practicum.shareit.user.dto.UserResponse;
import ru.practicum.shareit.user.mapper.UserMapper;
import ru.practicum.shareit.user.storage.UserStorage;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserStorage userStorage;
    private final UserMapper userMapper;

    @Override
    public UserResponse create(@Validated(UserRequest.Create.class) UserRequest userRequest) {
        if (userStorage.existsByEmail(userRequest.getEmail())) {
            throw new DuplicatedDataException("Email уже используется");
        }
        User user = userMapper.toUser(userRequest);
        User createdUser = userStorage.create(user);
        return userMapper.toUserResponse(createdUser);
    }

    @Override
    public UserResponse update(Long userId, @Validated(UserRequest.Update.class) UserRequest userRequest) {
        User existingUser = userStorage.findById(userId);

        if (userRequest.getEmail() != null) {
            validateEmailUniqueness(userRequest.getEmail(), userId);
        }

        userMapper.updateUserFromRequest(userRequest, existingUser);
        User updatedUser = userStorage.update(existingUser);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    public void delete(Long userId) {
        userStorage.delete(userId);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userStorage.findById(userId);
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> findAll() {
        return userStorage.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    public void validateEmailUniqueness(String email, Long userId) {
        if (userId == null) {
            if (userStorage.existsByEmail(email)) {
                throw new DuplicatedDataException("Email уже используется");
            }
        } else {
            if (userStorage.existsByEmailAndIdNot(email, userId)) {
                throw new DuplicatedDataException("Email уже используется другим пользователем");
            }
        }
    }
}