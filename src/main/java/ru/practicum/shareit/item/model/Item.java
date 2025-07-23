package ru.practicum.shareit.item.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.request.ItemRequest;

@Data
@EqualsAndHashCode(of = {"id"})
@NoArgsConstructor
public class Item {
    private Long id; //уникальный идентификатор предмета

    private String name; // название предмета

    private String description; // описание предмета

    private Boolean available; //статус доступно для аренды или нет

    private Long owner; // Владелец вещи

    private ItemRequest request; //если вещь была создана по запросу другого пользователя, то в этом
    //поле будет храниться ссылка на соответствующий запрос
}