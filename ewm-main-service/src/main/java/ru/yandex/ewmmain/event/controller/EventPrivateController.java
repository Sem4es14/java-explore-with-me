package ru.yandex.ewmmain.event.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.event.requestdto.EventCreateRequest;
import ru.yandex.ewmmain.event.requestdto.EventUpdateRequest;
import ru.yandex.ewmmain.event.responsedto.EventFullDto;
import ru.yandex.ewmmain.event.responsedto.EventShortDto;
import ru.yandex.ewmmain.event.service.EventService;
import ru.yandex.ewmmain.participationrequest.responsedto.RequestDto;
import ru.yandex.ewmmain.participationrequest.service.RequestService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/events")
@AllArgsConstructor
public class EventPrivateController {
    private final EventService eventService;
    private final RequestService requestService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByUser(@PathVariable("userId") @Positive Long userId,
                                                               @RequestParam(value = "from", defaultValue = "0")
                                                               @PositiveOrZero Integer from,
                                                               @RequestParam(value = "size", defaultValue = "10")
                                                               @Positive Integer size) {
        return ResponseEntity.of(Optional.of(eventService.getByUser(userId, from, size)));
    }

    @PatchMapping
    public ResponseEntity<EventFullDto> updateEventByUser(@PathVariable("userId") @Positive Long userId,
                                                          @RequestBody @Valid EventUpdateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.updateByUser(userId, request)));
    }


    @PostMapping
    public ResponseEntity<EventFullDto> createEvent(@PathVariable("userId") @Positive Long userId,
                                                    @RequestBody @Valid EventCreateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.createEvent(userId, request)));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventOfUserById(@PathVariable("userId") @Positive Long userId,
                                                           @PathVariable("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.getOfUserById(userId, eventId)));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> cancelEventByUser(@PathVariable("userId") @Positive Long userId,
                                                          @PathVariable("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.rejectByUser(userId, eventId)));
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<RequestDto>> getAllRequestOfEvent(@PathVariable("eventId") @Positive Long eventId,
                                                                 @PathVariable("userId") @Positive Long userId) {
        return ResponseEntity.of(Optional.of(requestService.getRequestsByEvent(userId, eventId)));
    }

    @PatchMapping("/{eventId}/requests/{requestId}/confirm")
    public ResponseEntity<RequestDto> confirmRequest(@PathVariable("userId") @Positive Long userId,
                                                     @PathVariable("eventId") @Positive Long eventId,
                                                     @PathVariable("requestId") @Positive Long requestId) {
        return ResponseEntity.of(Optional.of(requestService.approveRequest(userId, eventId, requestId)));
    }

    @PatchMapping("/{eventId}/requests/{requestId}/reject")
    public ResponseEntity<RequestDto> rejectRequest(@PathVariable("userId") @Positive Long userId,
                                                    @PathVariable("eventId") @Positive Long eventId,
                                                    @PathVariable("requestId") @Positive Long requestId) {
        return ResponseEntity.of(Optional.of(requestService.rejectRequest(userId, eventId, requestId)));
    }
}