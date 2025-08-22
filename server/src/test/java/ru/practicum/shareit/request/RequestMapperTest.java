package ru.practicum.shareit.request;

import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RequestMapperTest {

    private final RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);

    @Test
    void toEntity_ShouldMapCorrectly() {
        RequestDto dto = new RequestDto();
        dto.setDescription("Нужна дрель");

        User requestor = new User();
        requestor.setId(1L);
        requestor.setName("Test User");

        Request entity = requestMapper.toEntity(dto, requestor);

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

        Request request = new Request();
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

        RequestDto dto = requestMapper.toDto(request);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertEquals("Нужна дрель", dto.getDescription());
        assertNotNull(dto.getCreated());
        assertNotNull(dto.getItems());
        assertEquals(1, dto.getItems().size());
        assertEquals("Дрель", dto.getItems().get(0).getName());
    }

    @Test
    void toDto_WithNullItems_ShouldReturnEmptyList() {
        Request request = new Request();
        request.setId(1L);
        request.setDescription("Нужна дрель");
        request.setItems(null);

        RequestDto dto = requestMapper.toDto(request);

        assertNotNull(dto);
        assertEquals(1L, dto.getId());
        assertNotNull(dto.getItems());
        assertTrue(dto.getItems().isEmpty());
    }

    @Test
    void itemToResponseDto_ShouldMapCorrectly() {
        Request request = new Request();
        request.setId(1L);

        Item item = new Item();
        item.setId(1L);
        item.setName("Дрель");
        item.setDescription("Мощная дрель");
        item.setAvailable(true);
        item.setOwnerId(2L);
        item.setRequest(request);

        RequestDto.ItemResponseDto itemDto = requestMapper.itemToResponseDto(item);

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
        RequestDto.ItemResponseDto itemDto = requestMapper.itemToResponseDto(null);

        assertNull(itemDto);
    }

    @Test
    void itemToResponseDto_WithNullRequest_ShouldHandleCorrectly() {
        Item item = new Item();
        item.setId(1L);
        item.setRequest(null);

        RequestDto.ItemResponseDto itemDto = requestMapper.itemToResponseDto(item);

        assertNotNull(itemDto);
        assertNull(itemDto.getRequestId());
    }

    @Test
    void toEntity_ShouldIgnoreIdAndItems() {
        RequestDto dto = new RequestDto();
        dto.setId(999L);
        dto.setDescription("Нужна дрель");

        User requestor = new User();
        requestor.setId(1L);

        Request entity = requestMapper.toEntity(dto, requestor);

        assertNotNull(entity);
        assertNull(entity.getId());
        assertEquals("Нужна дрель", entity.getDescription());
        assertNull(entity.getItems());
    }

    @Test
    void toDto_WithEmptyItemsList_ShouldReturnEmptyList() {
        Request request = new Request();
        request.setId(1L);
        request.setDescription("Нужна дрель");
        request.setItems(List.of());

        RequestDto dto = requestMapper.toDto(request);

        assertNotNull(dto);
        assertNotNull(dto.getItems());
        assertTrue(dto.getItems().isEmpty());
    }
}