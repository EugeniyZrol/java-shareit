package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

public interface ItemService {
    ItemResponse addItem(ItemRequest itemRequest, Long ownerId);

    ItemResponse updateItem(Long itemId, ItemRequest itemRequest, Long ownerId);

    ItemResponse getItemById(Long itemId);

    List<ItemResponse> getAllItemsByOwner(Long ownerId);

    List<ItemResponse> searchAvailableItems(String text);

    CommentDto addComment(Long itemId, CommentDto commentDto, Long userId);
}