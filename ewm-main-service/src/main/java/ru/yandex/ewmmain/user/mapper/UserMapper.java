package ru.yandex.ewmmain.user.mapper;

import ru.yandex.ewmmain.user.responsedto.UserDto;
import ru.yandex.ewmmain.user.responsedto.UserShortDto;
import ru.yandex.ewmmain.user.model.User;

import java.util.List;
import java.util.stream.Collectors;

public class UserMapper {
    public static UserDto fromUserToDto(User user) {
        return new UserDto(
                user.getId(),
                user.getEmail(),
                user.getName()
        );
    }

    public static List<UserDto> fromUsersToDtos(List<User> users) {
        return users.stream()
                .map(UserMapper::fromUserToDto)
                .collect(Collectors.toList());
    }

    public static UserShortDto fromUserToShortDto(User user) {
        return new UserShortDto(
                user.getId(),
                user.getName()
        );
    }

    public static List<UserShortDto> fromUsersToShortDtos(List<User> users) {
        return users.stream()
                .map(UserMapper::fromUserToShortDto)
                .collect(Collectors.toList());
    }
}