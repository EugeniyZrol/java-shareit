package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.dto.ItemRequest;
import ru.practicum.shareit.item.dto.ItemResponse;
import ru.practicum.shareit.item.service.ItemService;

import java.util.List;

import static ru.practicum.shareit.constants.ShareItConstants.X_SHARER_USER_ID;

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
            @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        return itemService.getAllItemsByOwner(ownerId);
    }

    @PostMapping
    public ItemResponse addItem(@Validated(ItemRequest.Create.class) @RequestBody ItemRequest itemRequest,
                                @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        return itemService.addItem(itemRequest, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemResponse updateItem(@PathVariable Long itemId,
                                   @Validated(ItemRequest.Update.class) @RequestBody ItemRequest itemRequest,
                                   @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        return itemService.updateItem(itemId, itemRequest, ownerId);
    }

    @GetMapping("/search")
    public List<ItemResponse> searchItems(
            @RequestParam String text,
            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.searchAvailableItems(text);
    }
}