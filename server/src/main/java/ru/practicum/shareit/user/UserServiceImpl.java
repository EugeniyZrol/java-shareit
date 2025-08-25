package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.DuplicatedDataException;
import ru.practicum.shareit.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse create(UserRequest userRequest) {
        if (userRepository.existsByEmail(userRequest.getEmail())) {
            throw new DuplicatedDataException("Email уже используется");
        }

        User user = userMapper.toUser(userRequest);
        User createdUser = userRepository.save(user);
        return userMapper.toUserResponse(createdUser);
    }

    @Override
    @Transactional
    public UserResponse update(Long userId, UserRequest userRequest) {
        User existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));

        if (userRequest.getEmail() != null && !userRequest.getEmail().equals(existingUser.getEmail())) {
            validateEmailUniqueness(userRequest.getEmail(), userId);
            existingUser.setEmail(userRequest.getEmail());
        }

        if (userRequest.getName() != null) {
            existingUser.setName(userRequest.getName());
        }

        User updatedUser = userRepository.save(existingUser);
        return userMapper.toUserResponse(updatedUser);
    }

    @Override
    @Transactional
    public void delete(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
        userRepository.deleteById(userId);
    }

    @Override
    public UserResponse getUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        return userMapper.toUserResponse(user);
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toUserResponse)
                .collect(Collectors.toList());
    }

    private void validateEmailUniqueness(String email, Long userId) {
        if (userRepository.existsByEmailAndIdNot(email, userId)) {
            throw new DuplicatedDataException("Email уже используется другим пользователем");
        }
    }
}