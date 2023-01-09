package ru.yandex.ewmmain.user.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.user.responsedto.UserDto;
import ru.yandex.ewmmain.user.requestdto.UserCreateRequest;
import ru.yandex.ewmmain.user.service.UserService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/users")
@Validated
@AllArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserDto> createUser(@RequestBody @Valid UserCreateRequest request) {
        return ResponseEntity.of(Optional.of(userService.createUser(request)));
    }

    @DeleteMapping("/{userId}")
    public void deleteUser(@PathVariable("userId") @PositiveOrZero Long userId) {
        userService.deleteUser(userId);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> searchUsers(@RequestParam("ids") List<Long> ids,
                                                     @RequestParam(value = "from", defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(value = "size", defaultValue = "10") @Positive Integer size) {
        return ResponseEntity.of(Optional.of(userService.get(ids, from, size)));
    }
}