package ru.yandex.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.dto.user.NewUserRequest;
import ru.yandex.dto.user.UserDto;
import ru.yandex.dto.user.UserShortDto;
import ru.yandex.model.user.User;

@Component
public class UserMapper {

    public static UserDto toUserDto(User user) {
        return new UserDto(
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

    public static User toUser(NewUserRequest newUserRequest) {
        return new User(
                null,
                newUserRequest.getName(),
                newUserRequest.getEmail()
        );
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

}
