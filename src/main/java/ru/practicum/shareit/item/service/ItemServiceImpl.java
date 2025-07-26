package ru.practicum.shareit.item.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import ru.practicum.shareit.exception.NotFoundException;
import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.mapper.ItemMapper;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.item.storage.ItemStorage;
import ru.practicum.shareit.user.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ItemServiceImpl implements ItemService {
    private final ItemStorage itemStorage;
    private final UserService userService;
    private final ItemMapper itemMapper;

    @Override
    public ItemResponse addItem(@Validated(ItemRequest.Create.class) ItemRequest itemRequest, Long ownerId) {
        userService.getUserById(ownerId);

        Item item = itemMapper.toItem(itemRequest);
        item.setOwner(ownerId);
        Item savedItem = itemStorage.save(item);

        log.info("Добавлена новая вещь: {}", savedItem);
        return itemMapper.toItemResponse(savedItem);
    }

    @Override
    public ItemResponse updateItem(Long itemId, @Validated(ItemRequest.Update.class) ItemRequest itemRequest, Long ownerId) {
        Item existingItem = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));

        if (!existingItem.getOwner().equals(ownerId)) {
            throw new NotFoundException("Вещь с ID " + itemId + " не найдена у пользователя " + ownerId);
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

        Item updatedItem = itemStorage.save(existingItem);
        return itemMapper.toItemResponse(updatedItem);
    }

    @Override
    public ItemResponse getItemById(Long itemId) {
        Item item = itemStorage.findById(itemId)
                .orElseThrow(() -> new NotFoundException("Вещь с ID " + itemId + " не найдена"));
        return itemMapper.toItemResponse(item);
    }

    @Override
    public List<ItemResponse> getAllItemsByOwner(Long ownerId) {
        List<Item> items = itemStorage.findAllByOwner(ownerId);
        return items.stream()
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<ItemResponse> searchAvailableItems(String text) {
        if (text == null || text.isBlank()) {
            return Collections.emptyList();
        }

        String searchText = text.toLowerCase(Locale.ROOT);
        return itemStorage.findAll().stream()
                .filter(item -> item.getAvailable() != null && item.getAvailable())
                .filter(item -> item.getName() != null && item.getDescription() != null)
                .filter(item -> item.getName().toLowerCase(Locale.ROOT).contains(searchText) ||
                        item.getDescription().toLowerCase(Locale.ROOT).contains(searchText))
                .map(itemMapper::toItemResponse)
                .collect(Collectors.toList());
    }
}