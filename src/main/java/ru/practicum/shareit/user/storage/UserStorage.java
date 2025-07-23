package ru.practicum.shareit.user.storage;

import ru.practicum.shareit.user.User;

import java.util.Collection;

public interface UserStorage {

    User create(User user);

    User update(User user);

    void delete(Long userId);

    User findById(Long userId);

    Collection<User> findAll();

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);
}