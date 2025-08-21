package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.BookingRepository;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.exception.ValidationException;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.item.comment.CommentRepository;
import ru.practicum.shareit.request.Request;
import ru.practicum.shareit.request.RequestRepository;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserRepository;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ItemServiceImpl implements ItemService {

    private final ItemRepository itemRepository;
    private final UserRepository userRepository;
    private final RequestRepository itemRequestRepository;
    private final ItemMapper itemMapper;
    private final BookingRepository bookingRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public ItemResponse addItem(ItemRequest itemRequest, Long ownerId) {
        userRepository.findById(ownerId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + ownerId + " не найден"));

        Item item = itemMapper.toItem(itemRequest);
        item.setOwnerId(ownerId);

        if (itemRequest.getRequestId() != null) {
            Request request = itemRequestRepository.findById(itemRequest.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с ID " + itemRequest.getRequestId() + " не найден"));
            item.setRequest(request);
        }

        Item savedItem = itemRepository.save(item);
        return itemMapper.toItemResponse(savedItem);
    }

    @Override
    @Transactional
    public ItemResponse updateItem(Long itemId, ItemRequest itemRequest, Long ownerId) {
        Item existingItem = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));

        if (!existingItem.getOwnerId().equals(ownerId)) {
            throw new NotFoundException("Вещь с ID " + itemId + " не принадлежит пользователю " + ownerId);
        }

        if (itemRequest.getName() != null) {
            existingItem.setName(itemRequest.getName());
        }
        if (itemRequest.getDescription() != null) {
            existingItem.setDescription(itemRequest.getDescription());
        }
        if (itemRequest.getAvailable() != null) {
            existingItem.setAvailable(itemRequest.getAvailable());
        }

        if (itemRequest.getRequestId() != null) {
            Request request = itemRequestRepository.findById(itemRequest.getRequestId())
                    .orElseThrow(() -> new NotFoundException("Запрос с ID " + itemRequest.getRequestId() + " не найден"));
            existingItem.setRequest(request);
        } else if (itemRequest.getRequestId() == null && existingItem.getRequest() != null) {
            existingItem.setRequest(null);
        }

        Item updatedItem = itemRepository.save(existingItem);
        return itemMapper.toItemResponse(updatedItem);
    }

    @Override
    @Transactional(readOnly = true)
    public ItemResponse getItemById(Long itemId, Long userId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));

        List<CommentDto> comments = commentRepository.findByItemId(itemId).stream()
                .map(itemMapper::toCommentDto)
                .collect(Collectors.toList());

        ItemResponse response;
        if (item.getOwnerId().equals(userId)) {
            response = itemMapper.toItemResponse(item, getBookingsForItem(itemId));
        } else {
            response = itemMapper.toItemResponse(item);
        }
        response.setComments(comments);

        return response;
    }

    @Override
    public List<ItemResponse> getAllItemsByOwner(Long ownerId) {
        List<Item> items = itemRepository.findByOwnerId(ownerId);
        if (items.isEmpty()) {
            return Collections.emptyList();
        }

        List<Long> itemIds = items.stream().map(Item::getId).collect(Collectors.toList());

        Map<Long, List<Booking>> bookingsByItem = bookingRepository.findByItemIdIn(itemIds).stream()
                .collect(Collectors.groupingBy(booking -> booking.getItem().getId()));

        Map<Long, List<CommentDto>> commentsByItem = commentRepository.findByItemIn(items).stream()
                .collect(Collectors.groupingBy(
                        c -> c.getItem().getId(),
                        Collectors.mapping(itemMapper::toCommentDto, Collectors.toList())
                ));

        return items.stream()
                .map(item -> {
                    List<Booking> itemBookings = bookingsByItem.getOrDefault(item.getId(), Collections.emptyList());
                    ItemResponse response = itemMapper.toItemResponse(item, itemBookings);
                    response.setComments(commentsByItem.getOrDefault(item.getId(), Collections.emptyList()));
                    return response;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> searchAvailableItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String searchText = text.toLowerCase(Locale.ROOT);
        return itemRepository.searchAvailableItems(searchText).stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CommentDto addComment(Long itemId, CommentDto commentDto, Long userId) {
        User author = userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Пользователь с ID " + userId + " не найден"));
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));

        if (!bookingRepository.existsByBookerIdAndItemIdAndEndBefore(userId, itemId, LocalDateTime.now())) {
            throw new ValidationException("Пользователь не брал эту вещь в аренду или аренда еще не завершена");
        }

        Comment comment = itemMapper.toComment(commentDto, item, author);
        Comment savedComment = commentRepository.save(comment);
        log.info("Комментарий сохранён: ID={}, ItemID={}, AuthorID={}", savedComment.getId(), itemId, userId);
        return itemMapper.toCommentDto(savedComment);
    }

    private List<Booking> getBookingsForItem(Long itemId) {
        return bookingRepository.findByItemIdAndStatus(itemId, BookingStatus.APPROVED);
    }
}