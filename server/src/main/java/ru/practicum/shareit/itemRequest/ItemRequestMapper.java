package ru.practicum.shareit.itemRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface ItemRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "requestor", source = "requestor")
    @Mapping(target = "items", ignore = true)
    ItemRequest toEntity(ItemRequestDto dto, User requestor);

    @Mapping(target = "items", expression = "java(mapItemsToResponseDtos(request.getItems()))")
    ItemRequestDto toDto(ItemRequest request);

    default ItemResponseDto itemToResponseDto(Item item) {
        if (item == null) {
            return null;
        }

        return ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    default List<ItemResponseDto> mapItemsToResponseDtos(List<Item> items) {
        if (items == null) return List.of();
        return items.stream()
                .map(this::itemToResponseDto)
                .collect(Collectors.toList());
    }
}