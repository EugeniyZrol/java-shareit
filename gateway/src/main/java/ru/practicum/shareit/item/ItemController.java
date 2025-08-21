package ru.practicum.shareit.item;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static ru.practicum.shareit.constants.ShareItConstants.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemClient itemClient;

    @GetMapping("/{itemId}")
    public ResponseEntity<Object> getItemById(
            @PathVariable Long itemId,
            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Get item by ID: {}, user ID: {}", itemId, userId);
        return itemClient.getItemById(itemId, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getAllItemsByOwner(
            @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("Get all items for owner ID: {}", ownerId);
        return itemClient.getAllItemsByOwner(ownerId);
    }

    @PostMapping
    public ResponseEntity<Object> addItem(@Valid @RequestBody ItemRequest itemRequest,
                                          @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("Add item: {}, owner ID: {}", itemRequest, ownerId);
        return itemClient.addItem(itemRequest, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ResponseEntity<Object> updateItem(@PathVariable Long itemId,
                                             @Valid @RequestBody ItemRequest itemRequest,
                                             @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        log.info("Update item ID {}: {}, owner ID: {}", itemId, itemRequest, ownerId);
        return itemClient.updateItem(itemId, itemRequest, ownerId);
    }

    @GetMapping("/search")
    public ResponseEntity<Object> searchItems(
            @RequestParam String text,
            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Search items with text: {}, user ID: {}", text, userId);
        return itemClient.searchItems(text, userId);
    }

    @PostMapping("/{itemId}/comment")
    public ResponseEntity<Object> addComment(@PathVariable Long itemId,
                                             @Valid @RequestBody CommentDto commentDto,
                                             @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Add comment to item ID {}: {}, user ID: {}", itemId, commentDto, userId);
        return itemClient.addComment(itemId, commentDto, userId);
    }
}
