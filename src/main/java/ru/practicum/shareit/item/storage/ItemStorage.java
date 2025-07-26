package ru.practicum.shareit.item.storage;

import ru.practicum.shareit.item.model.Item;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ItemStorage {
    Item save(Item item);

    Optional<Item> findById(Long itemId);

    List<Item> findAllByOwner(Long ownerId);

    Collection<Item> findAll();
}