package ru.practicum.shareit.item;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.item.comment.CommentDto;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.practicum.shareit.constants.ShareItConstants.X_SHARER_USER_ID;

@WebMvcTest(ItemController.class)
class ItemControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ItemService itemService;

    private ItemDtoResponse itemResponse;
    private ItemDtoRequest itemDtoRequest;
    private CommentDto commentDto;

    @BeforeEach
    void setUp() {
        itemResponse = new ItemDtoResponse(
                1L, "Test Item", "Test Description", true, 1L, null,
                null, null, Collections.emptyList()
        );

        itemDtoRequest = new ItemDtoRequest("Test Item", "Test Description", true, null);

        commentDto = new CommentDto();
        commentDto.setId(1L);
        commentDto.setText("Great item!");
        commentDto.setAuthorName("Test User");
        commentDto.setCreated(LocalDateTime.now());
    }

    @Test
    void getItemById_ShouldReturnItem() throws Exception {
        when(itemService.getItemById(anyLong(), anyLong())).thenReturn(itemResponse);

        mockMvc.perform(get("/items/1")
                        .header(X_SHARER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Item"))
                .andExpect(jsonPath("$.description").value("Test Description"));
    }

    @Test
    void getAllItemsByOwner_ShouldReturnItems() throws Exception {
        when(itemService.getAllItemsByOwner(anyLong())).thenReturn(List.of(itemResponse));

        mockMvc.perform(get("/items")
                        .header(X_SHARER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Item"));
    }

    @Test
    void addItem_ShouldCreateItem() throws Exception {
        when(itemService.addItem(any(ItemDtoRequest.class), anyLong())).thenReturn(itemResponse);

        mockMvc.perform(post("/items")
                        .header(X_SHARER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    void updateItem_ShouldUpdateItem() throws Exception {
        when(itemService.updateItem(anyLong(), any(ItemDtoRequest.class), anyLong())).thenReturn(itemResponse);

        mockMvc.perform(patch("/items/1")
                        .header(X_SHARER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(itemDtoRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Item"));
    }

    @Test
    void searchItems_ShouldReturnMatchingItems() throws Exception {
        when(itemService.searchAvailableItems(anyString())).thenReturn(List.of(itemResponse));

        mockMvc.perform(get("/items/search")
                        .param("text", "test")
                        .header(X_SHARER_USER_ID, "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Test Item"));
    }

    @Test
    void addComment_ShouldCreateComment() throws Exception {
        when(itemService.addComment(anyLong(), any(CommentDto.class), anyLong())).thenReturn(commentDto);

        mockMvc.perform(post("/items/1/comment")
                        .header(X_SHARER_USER_ID, "1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.text").value("Great item!"))
                .andExpect(jsonPath("$.authorName").value("Test User"));
    }

    @Test
    void getItemById_WithoutUserIdHeader_ShouldReturnBadRequest() throws Exception {
        mockMvc.perform(get("/items/1"))
                .andExpect(status().isBadRequest());
    }
}