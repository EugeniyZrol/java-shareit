package ru.practicum.shareit.booking;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemResponse;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserResponse;

@Mapper(componentModel = "spring")
public interface BookingMapper {

    @Mapping(target = "item", source = "item")
    @Mapping(target = "booker", source = "booker")
    BookingResponseDto toBookingResponseDto(Booking booking);

    default Booking toBooking(BookingRequestDto bookingRequestDto, Long bookerId) {
        if (bookingRequestDto == null) {
            return null;
        }

        Booking booking = new Booking();
        booking.setStart(bookingRequestDto.getStart());
        booking.setEnd(bookingRequestDto.getEnd());
        booking.setStatus(BookingStatus.WAITING);
        booking.setItem(mapIdToItem(bookingRequestDto.getItemId()));
        booking.setBooker(mapIdToUser(bookerId));

        return booking;
    }

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

    default ItemResponse mapItemToDto(Item item) {
        if (item == null) {
            return null;
        }
        ItemResponse dto = new ItemResponse();
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