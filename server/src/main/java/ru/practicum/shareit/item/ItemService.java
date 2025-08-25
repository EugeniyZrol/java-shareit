package ru.practicum.shareit.item;

import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

public interface ItemService {
    ItemDtoResponse addItem(ItemDtoRequest itemRequest, Long ownerId);

    ItemDtoResponse updateItem(Long itemId, ItemDtoRequest itemRequest, Long ownerId);

    ItemDtoResponse getItemById(Long itemId, Long userId);

    List<ItemDtoResponse> getAllItemsByOwner(Long ownerId);

    List<ItemDtoResponse> searchAvailableItems(String text);

    CommentDto addComment(Long itemId, CommentDto commentDto, Long userId);
}