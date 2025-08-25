package ru.practicum.shareit.item;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.practicum.shareit.item.comment.CommentDto;

import java.util.List;

import static ru.practicum.shareit.constants.ShareItConstants.X_SHARER_USER_ID;

@RestController
@RequestMapping("/items")
@RequiredArgsConstructor
public class ItemController {
    private final ItemService itemService;

    @GetMapping("/{itemId}")
    public ItemDtoResponse getItemById(
            @PathVariable Long itemId,
            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.getItemById(itemId, userId);
    }

    @GetMapping
    public List<ItemDtoResponse> getAllItemsByOwner(
            @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        return itemService.getAllItemsByOwner(ownerId);
    }

    @PostMapping
    public ItemDtoResponse addItem(@RequestBody ItemDtoRequest itemRequest, // @Validated удален
                                @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        return itemService.addItem(itemRequest, ownerId);
    }

    @PatchMapping("/{itemId}")
    public ItemDtoResponse updateItem(@PathVariable Long itemId,
                                   @RequestBody ItemDtoRequest itemRequest, // @Validated удален
                                   @RequestHeader(X_SHARER_USER_ID) Long ownerId) {
        return itemService.updateItem(itemId, itemRequest, ownerId);
    }

    @GetMapping("/search")
    public List<ItemDtoResponse> searchItems(
            @RequestParam String text,
            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.searchAvailableItems(text);
    }

    @PostMapping("/{itemId}/comment")
    public CommentDto addComment(@PathVariable Long itemId,
                                 @RequestBody CommentDto commentDto, // @Valid удален
                                 @RequestHeader(X_SHARER_USER_ID) Long userId) {
        return itemService.addComment(itemId, commentDto, userId);
    }
}