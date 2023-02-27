package ru.yandex.ewmmain.participationrequest.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.participationrequest.responsedto.RequestDto;
import ru.yandex.ewmmain.participationrequest.service.RequestService;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/requests")
public class RequestController {
    private final RequestService requestService;

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping
    public ResponseEntity<List<RequestDto>> getAllByUser(@PathVariable("userId") @PositiveOrZero Long userId) {
        return ResponseEntity.of(Optional.of(requestService.getByUser(userId)));
    }

    @PostMapping
    public ResponseEntity<RequestDto> createRequest(@PathVariable("userId") @PositiveOrZero Long userId,
                                                    @RequestParam("eventId") @PositiveOrZero Long eventId) {
        return ResponseEntity.of(Optional.of(requestService.createRequest(userId, eventId)));
    }

    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<RequestDto> cancelRequestByUser(@PathVariable("userId") @PositiveOrZero Long userId,
                                                          @PathVariable("requestId") @PositiveOrZero Long requestId) {
        return ResponseEntity.of(Optional.of(requestService.rejectRequestByUser(userId, requestId)));
    }
}