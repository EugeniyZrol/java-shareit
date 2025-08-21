package ru.practicum.shareit.request;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

import static ru.practicum.shareit.constants.ShareItConstants.X_SHARER_USER_ID;

@Slf4j
@RestController
@RequestMapping(path = "/requests")
@RequiredArgsConstructor
public class RequestController {
    private final RequestClient requestClient;

    @PostMapping
    public ResponseEntity<Object> create(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @Valid @RequestBody RequestDto requestDto) {
        log.info("Create request by user ID: {}", userId);
        return requestClient.createRequest(requestDto, userId);
    }

    @GetMapping
    public ResponseEntity<Object> getOwnRequests(
            @RequestHeader(X_SHARER_USER_ID) Long userId) {
        log.info("Get own requests for user ID: {}", userId);
        return requestClient.getOwnRequests(userId);
    }

    @GetMapping("/all")
    public ResponseEntity<Object> getAllRequests(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @RequestParam(value = "from", required = false) @PositiveOrZero Integer from,
            @RequestParam(value = "size", required = false) @Positive Integer size) {
        log.info("Get all requests for user ID: {}, from: {}, size: {}", userId, from, size);
        return requestClient.getAllRequests(userId, from, size);
    }

    @GetMapping("/{requestId}")
    public ResponseEntity<Object> getRequestById(
            @RequestHeader(X_SHARER_USER_ID) Long userId,
            @PathVariable Long requestId) {
        log.info("Get request by ID: {}, user ID: {}", requestId, userId);
        return requestClient.getRequestById(requestId, userId);
    }
}