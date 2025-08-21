package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {

    RequestDto create(RequestDto itemRequestDto, Long userId);

    List<RequestDto> getOwnRequests(Long userId);

    List<RequestDto> getAllRequests(Long userId, Integer from, Integer size);

    RequestDto getRequestById(Long requestId, Long userId);
}
