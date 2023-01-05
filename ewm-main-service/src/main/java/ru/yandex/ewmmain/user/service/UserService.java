package ru.yandex.ewmmain.user.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.ewmmain.exception.model.AlreadyExists;
import ru.yandex.ewmmain.exception.model.NotFoundException;
import ru.yandex.ewmmain.user.responsedto.UserDto;
import ru.yandex.ewmmain.user.mapper.UserMapper;
import ru.yandex.ewmmain.user.model.User;
import ru.yandex.ewmmain.user.repository.UserRepository;
import ru.yandex.ewmmain.user.requestdto.UserCreateRequest;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    public UserDto createUser(UserCreateRequest request) {
        if (userRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new AlreadyExists("Email " + request.getEmail() + "is already exists");
        }
        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail());

        return UserMapper.fromUserToDto(userRepository.save(user));
    }

    public void deleteUser(Long userId) {
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User with id " + userId + " is not found");
        }
        userRepository.deleteById(userId);
    }

    public List<UserDto> get(List<Long> ids, Integer from, Integer size) {
        return UserMapper.fromUsersToDtos(ids.stream()
                .map(userRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .skip(from)
                .limit(size)
                .collect(Collectors.toList()));
    }
}
