package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemResponse getItemById(@PathVariable Long itemId) {
        return itemService.getItemById(itemId);
    }

    @GetMapping
    public List<ItemResponse> getAllItemsByOwner(
            @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.getAllItemsByOwner(ownerId);
    }

    @PostMapping
    public ItemResponse addItem(@Valid @RequestBody ItemRequest itemRequest,
                                @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.addItem(itemRequest, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponse updateItem(@PathVariable Long itemId,
                                   @RequestBody ItemRequest itemRequest,
                                   @RequestHeader("X-Sharer-User-Id") Long ownerId) {
        return itemService.updateItem(itemId, itemRequest, ownerId);
    }

    @GetMapping("/search")
    public List<ItemResponse> searchItems(
            @RequestParam String text,
            @RequestHeader("X-Sharer-User-Id") Long userId) {
        return itemService.searchAvailableItems(text);
    }
}