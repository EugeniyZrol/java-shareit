package ru.practicum.shareit.itemRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class ItemRequestController {
    private final ItemRequestService itemRequestService;
    private static final String USER_ID_HEADER = "X-Sharer-User-Id";

    @PostMapping
    public ResponseEntity<ItemRequestDto> create(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestBody ItemRequestDto itemRequestDto) { // @Validated удален
        return ResponseEntity.ok(itemRequestService.create(itemRequestDto, userId));
    }

    @GetMapping
    public ResponseEntity<List<ItemRequestDto>> getOwnRequests(
            @RequestHeader(USER_ID_HEADER) Long userId) {
        return ResponseEntity.ok(itemRequestService.getOwnRequests(userId));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ItemRequestDto>> getAllRequests(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @RequestParam(value = "from", required = false) Integer from,
            @RequestParam(value = "size", required = false) Integer size) {
        return ResponseEntity.ok(itemRequestService.getAllRequests(userId, from, size));
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<ItemRequestDto> getRequestById(
            @RequestHeader(USER_ID_HEADER) Long userId,
            @PathVariable Long requestId) {
        return ResponseEntity.ok(itemRequestService.getRequestById(requestId, userId));
    }
}