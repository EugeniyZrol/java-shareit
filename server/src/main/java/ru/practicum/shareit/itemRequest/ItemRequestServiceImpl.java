package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemRequestServiceImpl implements ItemRequestService {
    private final ItemRequestRepository itemRequestRepository;
    private final UserRepository userRepository;
    private final ItemRequestMapper requestMapper;

    @Override
    @Transactional
    public ItemRequestDto create(ItemRequestDto requestDto, Long userId) {
        User requestor = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        ItemRequest request = requestMapper.toEntity(requestDto, requestor);
        request.setCreated(LocalDateTime.now());
        ItemRequest savedRequest = itemRequestRepository.save(request);
        return requestMapper.toDto(savedRequest);
    }

    @Override
    public List<ItemRequestDto> getOwnRequests(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
        List<ItemRequest> requests = itemRequestRepository.findByRequestorIdOrderByCreatedDesc(userId);
        return requests.stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemRequestDto> getAllRequests(Long userId, Integer from, Integer size) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }

        int page = from == null || size == null ? 0 : from / size;
        int pageSize = size == null ? Integer.MAX_VALUE : size;

        Pageable pageable = PageRequest.of(page, pageSize, Sort.by("created").descending());
        Page<ItemRequest> pageResult = itemRequestRepository.findAllByRequestorIdNot(userId, pageable);

        return pageResult.getContent().stream()
                .map(requestMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public ItemRequestDto getRequestById(Long requestId, Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("Пользователь с ID " + userId + " не найден");
        }
        ItemRequest itemRequest = itemRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("Запрос с ID " + requestId + " не найден"));
        return requestMapper.toDto(itemRequest);
    }
}