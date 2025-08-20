package ru.practicum.shareit.item;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Mapper(componentModel = "spring")
public interface ItemMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "request", ignore = true) // Обрабатывается в сервисе
    @Mapping(target = "comments", ignore = true)
    @Mapping(target = "name", source = "itemRequest.name")
    @Mapping(target = "description", source = "itemRequest.description")
    @Mapping(target = "available", source = "itemRequest.available")
    Item toItem(ItemRequest itemRequest);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "available", source = "item.available")
    @Mapping(target = "ownerId", source = "item.ownerId")
    @Mapping(target = "requestId", expression = "java(item.getRequest() != null ? item.getRequest().getId() : null)")
    @Mapping(target = "lastBooking", source = "bookings", qualifiedByName = "mapLastBooking")
    @Mapping(target = "nextBooking", source = "bookings", qualifiedByName = "mapNextBooking")
    @Mapping(target = "comments", ignore = true)
    ItemResponse toItemResponse(Item item, List<Booking> bookings);

    @Mapping(target = "id", source = "item.id")
    @Mapping(target = "name", source = "item.name")
    @Mapping(target = "description", source = "item.description")
    @Mapping(target = "available", source = "item.available")
    @Mapping(target = "ownerId", source = "item.ownerId")
    @Mapping(target = "requestId", expression = "java(item.getRequest() != null ? item.getRequest().getId() : null)")
    @Mapping(target = "lastBooking", ignore = true)
    @Mapping(target = "nextBooking", ignore = true)
    @Mapping(target = "comments", ignore = true)
    ItemResponse toItemResponse(Item item);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", source = "item")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    Comment toComment(CommentDto commentDto, Item item, User author);

    @Named("mapLastBooking")
    default ItemResponse.BookingInfo mapLastBooking(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings.stream()
                .filter(booking -> booking.getStart().isBefore(LocalDateTime.now()))
                .max(Comparator.comparing(Booking::getStart))
                .map(this::convertToBookingInfo)
                .orElse(null);
    }

    @Named("mapNextBooking")
    default ItemResponse.BookingInfo mapNextBooking(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            return null;
        }

        return bookings.stream()
                .filter(booking -> booking.getStart().isAfter(LocalDateTime.now()))
                .min(Comparator.comparing(Booking::getStart))
                .map(this::convertToBookingInfo)
                .orElse(null);
    }

    private ItemResponse.BookingInfo convertToBookingInfo(Booking booking) {
        if (booking == null || booking.getBooker() == null) {
            return null;
        }
        return new ItemResponse.BookingInfo(booking.getId(), booking.getBooker().getId());
    }
}