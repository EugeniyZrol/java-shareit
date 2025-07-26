package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Item {
    private Long id;
    private String name;
    private String description;
    private Boolean available;
    private Long owner;
    private ItemRequest request;

    public ItemRequest getRequest() {
        return request == null ? null : new ItemRequest(request);
    }

    public void setRequest(ItemRequest request) {
        this.request = request == null ? null : new ItemRequest(request);
    }
}