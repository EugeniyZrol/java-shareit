package ru.practicum.shareit.item.service;

import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemResponse;

import java.util.List;

public interface ItemService {
    ItemResponse addItem(ItemRequest itemRequest, Long ownerId);

    ItemResponse updateItem(Long itemId, ItemRequest itemRequest, Long ownerId);

    ItemResponse getItemById(Long itemId);

    List<ItemResponse> getAllItemsByOwner(Long ownerId);

    List<ItemResponse> searchAvailableItems(String text);
}