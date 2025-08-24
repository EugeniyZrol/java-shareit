package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDtoResponse;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserResponse;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "item", source = "item")
    @Mapping(target = "booker", source = "booker")
    BookingResponseDto toBookingResponseDto(Booking booking);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "status", constant = "WAITING")
    @Mapping(target = "item", expression = "java(mapIdToItem(dto.getItemId()))")
    @Mapping(target = "booker", expression = "java(mapIdToUser(bookerId))")
    @Mapping(target = "start", source = "dto.start")
    @Mapping(target = "end", source = "dto.end")
    Booking toBooking(BookingRequestDto dto, Long bookerId);

    default Item mapIdToItem(Long itemId) {
        if (itemId == null) {
            return null;
        }
        Item item = new Item();
        item.setId(itemId);
        return item;
    }

    default User mapIdToUser(Long userId) {
        if (userId == null) {
            return null;
        }
        User user = new User();
        user.setId(userId);
        return user;
    }

    default ItemDtoResponse mapItemToDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemDtoResponse dto = new ItemDtoResponse();
        dto.setId(item.getId());
        dto.setName(item.getName());
        return dto;
    }

    default UserResponse mapUserToDto(User user) {
        if (user == null) {
            return null;
        }
        UserResponse dto = new UserResponse();
        dto.setId(user.getId());
        return dto;
    }
}