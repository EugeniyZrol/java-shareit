package ru.practicum.shareit.itemRequest;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ItemRequestMapperTest {

    private final ItemRequestMapper requestMapper = Mappers.getMapper(ItemRequestMapper.class);

    @Test
    void toEntity_ShouldMapCorrectly() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setDescription("Нужна дрель");

        User requestor = new User();
        requestor.setId(1L);
        requestor.setName("Test User");

        ItemRequest entity = requestMapper.toEntity(dto, requestor);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Нужна дрель", entity.getDescription());
        assertEquals(requestor, entity.getRequestor());
        assertNotNull(entity.getCreated()); // Теперь ожидаем не null, а установленное значение
        assertNull(entity.getItems()); // Должно игнорироваться
    }

    @Test
    void toDto_ShouldMapCorrectly() {
        User requestor = new User();
        requestor.setId(1L);
        requestor.setName("Test User");

        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Нужна дрель");
        request.setRequestor(requestor);
        request.setCreated(LocalDateTime.now());

        Item item = new Item();
        item.setId(1L);
        item.setName("Дрель");
        item.setDescription("Мощная дрель");
        item.setAvailable(true);
        item.setOwnerId(2L);
        item.setRequest(request);

        request.setItems(List.of(item));

        ItemRequestDto dto = requestMapper.toDto(request);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Нужна дрель", dto.getDescription());
        assertNotNull(dto.getCreated());
        assertNotNull(dto.getItems());
        assertEquals(1, dto.getItems().size());
        assertEquals("Дрель", dto.getItems().getFirst().getName());
    }

    @Test
    void toDto_WithNullItems_ShouldReturnEmptyList() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Нужна дрель");
        request.setItems(null);

        ItemRequestDto dto = requestMapper.toDto(request);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNotNull(dto.getItems());
        assertTrue(dto.getItems().isEmpty());
    }

    @Test
    void itemToResponseDto_ShouldMapCorrectly() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Дрель");
        item.setDescription("Мощная дрель");
        item.setAvailable(true);
        item.setOwnerId(2L);
        item.setRequest(request);

        ItemResponseDto itemDto = requestMapper.itemToResponseDto(item);

        assertNotNull(itemDto);
        assertEquals(1L, itemDto.getId());
        assertEquals("Дрель", itemDto.getName());
        assertEquals("Мощная дрель", itemDto.getDescription());
        assertTrue(itemDto.getAvailable());
        assertEquals(2L, itemDto.getOwnerId());
        assertEquals(1L, itemDto.getRequestId());
    }

    @Test
    void itemToResponseDto_WithNullItem_ShouldReturnNull() {
        ItemResponseDto itemDto = requestMapper.itemToResponseDto(null);

        assertNull(itemDto);
    }

    @Test
    void itemToResponseDto_WithNullRequest_ShouldHandleCorrectly() {
        Item item = new Item();
        item.setId(1L);
        item.setRequest(null);

        ItemResponseDto itemDto = requestMapper.itemToResponseDto(item);

        assertNotNull(itemDto);
        assertNull(itemDto.getRequestId());
    }

    @Test
    void toEntity_ShouldIgnoreIdAndItems() {
        ItemRequestDto dto = new ItemRequestDto();
        dto.setId(999L);
        dto.setDescription("Нужна дрель");

        User requestor = new User();
        requestor.setId(1L);

        ItemRequest entity = requestMapper.toEntity(dto, requestor);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Нужна дрель", entity.getDescription());
        assertNull(entity.getItems());
    }

    @Test
    void toDto_WithEmptyItemsList_ShouldReturnEmptyList() {
        ItemRequest request = new ItemRequest();
        request.setId(1L);
        request.setDescription("Нужна дрель");
        request.setItems(List.of());

        ItemRequestDto dto = requestMapper.toDto(request);

        assertNotNull(dto);
        assertNotNull(dto.getItems());
        assertTrue(dto.getItems().isEmpty());
    }
}