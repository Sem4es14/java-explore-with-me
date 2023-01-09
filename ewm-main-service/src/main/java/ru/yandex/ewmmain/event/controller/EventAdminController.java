package ru.yandex.ewmmain.event.controller;

import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.event.model.EventState;
import ru.yandex.ewmmain.event.requestdto.EventAdminUpdateRequest;
import ru.yandex.ewmmain.event.requestdto.filterparam.SearchParam;
import ru.yandex.ewmmain.event.responsedto.EventFullDto;
import ru.yandex.ewmmain.event.service.EventService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/admin/events")
@AllArgsConstructor
public class EventAdminController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> searchByAdmin(@RequestParam(value = "users", required = false)
                                                            List<Long> users,
                                                            @RequestParam(value = "states", required = false)
                                                            List<EventState> states,
                                                            @RequestParam(value = "categories", required = false)
                                                            List<Long> categories,
                                                            @RequestParam(value = "rangeStart", required = false)
                                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                            LocalDateTime rangeStart,
                                                            @RequestParam(value = "rangeEnd", required = false)
                                                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                            LocalDateTime rangeEnd,
                                                            @RequestParam(value = "from", defaultValue = "0")
                                                            @PositiveOrZero Long from,
                                                            @RequestParam(value = "size", defaultValue = "10")
                                                            @Positive Long size) {
        SearchParam param = new SearchParam();
        param.setUsers(users);
        param.setStates(states);
        param.setCategories(categories);
        param.setRangeStart(rangeStart);
        param.setRangeEnd(rangeEnd);

        return ResponseEntity.of(Optional.of(eventService.searchByAdmin(param, from, size)));
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventByAdmin(@PathVariable("eventId") @Positive Long eventId,
                                                           @RequestBody @Valid EventAdminUpdateRequest request) {
        return ResponseEntity.of(Optional.of(eventService.updateByAdmin(eventId, request)));
    }

    @PatchMapping("/{eventId}/publish")
    public ResponseEntity<EventFullDto> publishEvent(@PathVariable("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.publish(eventId)));
    }

    @PatchMapping("/{eventId}/reject")
    public ResponseEntity<EventFullDto> rejectEvent(@PathVariable("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(eventService.rejectByAdmin(eventId)));
    }
}