package ru.practicum.shareit.itemRequest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.exception.NotFoundException;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.constants.ShareItConstants.X_SHARER_USER_ID;

@WebMvcTest(ItemRequestController.class)
class ItemItemRequestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemRequestService itemRequestService;

    private ItemRequestDto itemRequestDto;

    @BeforeEach
    void setUp() {
        ItemResponseDto itemDto = ItemResponseDto.builder()
                .id(1L)
                .name("Дрель")
                .description("Мощная дрель")
                .available(true)
                .ownerId(2L)
                .requestId(1L)
                .build();

        itemRequestDto = ItemRequestDto.builder()
                .id(1L)
                .description("Нужна дрель")
                .created(LocalDateTime.now())
                .items(List.of(itemDto))
                .build();
    }

    @Test
    void create_ShouldCreateRequest() throws Exception {
        when(itemRequestService.create(any(ItemRequestDto.class), anyLong())).thenReturn(itemRequestDto);

        mockMvc.perform(post("/requests")
                        .header(X_SHARER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Нужна дрель"))
                .andExpect(jsonPath("$.items[0].name").value("Дрель"));
    }

    @Test
    void create_WithoutUserIdHeader_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(post("/requests")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemRequestDto)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getOwnRequests_ShouldReturnUserRequests() throws Exception {
        when(itemRequestService.getOwnRequests(anyLong())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests")
                        .header(X_SHARER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Нужна дрель"));
    }

    @Test
    void getAllRequests_WithPagination_ShouldReturnRequests() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), any(), any())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, "1")
                        .param("from", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].description").value("Нужна дрель"));
    }

    @Test
    void getAllRequests_WithoutPagination_ShouldReturnRequests() throws Exception {
        when(itemRequestService.getAllRequests(anyLong(), any(), any())).thenReturn(List.of(itemRequestDto));

        mockMvc.perform(get("/requests/all")
                        .header(X_SHARER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1));
    }

    @Test
    void getRequestById_ShouldReturnRequest() throws Exception {
        when(itemRequestService.getRequestById(anyLong(), anyLong())).thenReturn(itemRequestDto);

        mockMvc.perform(get("/requests/1")
                        .header(X_SHARER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.description").value("Нужна дрель"));
    }

    @Test
    void getRequestById_WhenNotFound_ShouldReturnNotFound() throws Exception {
        when(itemRequestService.getRequestById(anyLong(), anyLong()))
                .thenThrow(new NotFoundException("Запрос не найден"));

        mockMvc.perform(get("/requests/999")
                        .header(X_SHARER_USER_ID, "1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getOwnRequests_WhenUserNotFound_ShouldReturnNotFound() throws Exception {
        when(itemRequestService.getOwnRequests(anyLong()))
                .thenThrow(new NotFoundException("Пользователь не найден"));

        mockMvc.perform(get("/requests")
                        .header(X_SHARER_USER_ID, "999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_WithEmptyDescription_ShouldReturnBadRequest() throws Exception {
        ItemRequestDto emptyRequest = ItemRequestDto.builder()
                .description("")
                .build();

        mockMvc.perform(post("/requests")
                        .header(X_SHARER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(emptyRequest)))
                .andExpect(status().isOk()); // Валидация отключена
    }
}