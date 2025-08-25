package ru.practicum.shareit.itemRequest;

import java.util.List;

public interface ItemRequestService {

    ItemRequestDto create(ItemRequestDto itemRequestDto, Long userId);

    List<ItemRequestDto> getOwnRequests(Long userId);

    List<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size);

    ItemRequestDto getRequestById(Long requestId, Long userId);
}
