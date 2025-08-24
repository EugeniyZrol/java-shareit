package ru.practicum.shareit.item;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;
import ru.practicum.shareit.client.BaseClient;

import java.util.Map;

@Service
public class ItemClient extends BaseClient {
    private static final String API_PREFIX = "/items";

    @Autowired
    public ItemClient(@Value("${shareit-server.url:http://localhost:9090}") String serverUrl, RestTemplateBuilder builder) {
        super(
                builder
                        .uriTemplateHandler(new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                        .build()
        );
    }

    public ResponseEntity<Object> getItemById(Long itemId, Long userId) {
        return get("/" + itemId, userId);
    }

    public ResponseEntity<Object> getAllItemsByOwner(Long ownerId) {
        return get("", ownerId);
    }

    public ResponseEntity<Object> addItem(ItemDtoRequest itemDtoRequest, Long ownerId) {
        return post("", ownerId, itemDtoRequest);
    }

    public ResponseEntity<Object> updateItem(Long itemId, ItemDtoRequest itemDtoRequest, Long ownerId) {
        return patch("/" + itemId, ownerId, itemDtoRequest);
    }

    public ResponseEntity<Object> searchItems(String text, Long userId) {
        Map<String, Object> parameters = Map.of("text", text);
        return get("/search?text={text}", userId, parameters);
    }

    public ResponseEntity<Object> addComment(Long itemId, Object commentDto, Long userId) {
        return post("/" + itemId + "/comment", userId, commentDto);
    }
}