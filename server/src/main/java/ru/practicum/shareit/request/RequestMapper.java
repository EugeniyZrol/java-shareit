package ru.practicum.shareit.request;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface RequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "created", ignore = true)
    @Mapping(target = "requestor", source = "requestor")
    @Mapping(target = "items", ignore = true)
    Request toEntity(RequestDto dto, User requestor);

    @Mapping(target = "items", expression = "java(mapItemsToResponseDtos(request.getItems()))")
    RequestDto toDto(Request request);

    default RequestDto.ItemResponseDto itemToResponseDto(Item item) {
        if (item == null) {
            return null;
        }

        return RequestDto.ItemResponseDto.builder()
                .id(item.getId())
                .name(item.getName())
                .description(item.getDescription())
                .available(item.getAvailable())
                .ownerId(item.getOwnerId())
                .requestId(item.getRequest() != null ? item.getRequest().getId() : null)
                .build();
    }

    default List<RequestDto.ItemResponseDto> mapItemsToResponseDtos(List<Item> items) {
        if (items == null) return List.of();
        return items.stream()
                .map(this::itemToResponseDto)
                .collect(Collectors.toList());
    }
}