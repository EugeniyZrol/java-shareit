package ru.practicum.shareit.request;

import java.util.List;

public interface RequestService {

    public RequestDto create(RequestDto itemRequestDto, Long userId);

    public List<RequestDto> getOwnRequests(Long userId);

    public List<RequestDto> getAllRequests(Long userId, Integer from, Integer size);

    public RequestDto getRequestById(Long requestId, Long userId);
}
