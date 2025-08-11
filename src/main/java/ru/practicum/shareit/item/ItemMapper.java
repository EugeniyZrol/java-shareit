package ru.practicum.shareit.item;

import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import ru.practicum.shareit.booking.BookingInfoContext;
import ru.practicum.shareit.item.comment.Comment;
import ru.practicum.shareit.item.comment.CommentDto;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface ItemMapper {
    @Mapping(target = "ownerId", source = "ownerId")
    @Mapping(target = "requestId", source = "request.id")
    @Mapping(target = "lastBooking", source = ".", qualifiedByName = "lastBooking")
    @Mapping(target = "nextBooking", source = ".", qualifiedByName = "nextBooking")
    @Mapping(target = "comments", source = "item.comments")
    ItemResponse toItemResponse(Item item, @Context BookingInfoContext context);

    @Mapping(target = "ownerId", ignore = true)
    @Mapping(target = "request", ignore = true)
    @Mapping(target = "id", ignore = true)
    Item toItem(ItemRequest itemRequest);

    @Mapping(target = "authorName", source = "author.name")
    CommentDto toCommentDto(Comment comment);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "item", source = "item")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "created", expression = "java(java.time.LocalDateTime.now())")
    Comment toComment(CommentDto commentDto, Item item, User author);

    @Named("lastBooking")
    default ItemResponse.BookingInfo mapLastBooking(Item item, @Context BookingInfoContext context) {
        if (context == null) {
            return null;
        }
        ItemResponse.BookingInfo lastBooking = context.getLastBooking(item.getId());
        if (lastBooking != null && lastBooking.getEnd().isBefore(LocalDateTime.now())) {
            return null;
        }
        return lastBooking;
    }

    @Named("nextBooking")
    default ItemResponse.BookingInfo mapNextBooking(Item item, @Context BookingInfoContext context) {
        if (context == null) {
            return null;
        }
        return context.getNextBooking(item.getId());
    }

    @Mapping(target = "comments", ignore = true)
    default ItemResponse toItemResponse(Item item) {
        return toItemResponse(item, null);
    }
}