package ru.practicum.shareit.user;

import org.mapstruct.*;

@Mapper(componentModel = "spring")
public interface UserMapper {
    UserResponse toUserResponse(User user);

    @Mapping(target = "id", ignore = true)
    User toUser(UserRequest userRequest);
}