package ru.yandex.ewmmain.event.controller;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.event.requestdto.filterparam.SearchParam;
import ru.yandex.ewmmain.event.requestdto.filterparam.SortType;
import ru.yandex.ewmmain.event.responsedto.EventFullDto;
import ru.yandex.ewmmain.event.responsedto.EventShortDto;
import ru.yandex.ewmmain.event.service.EventService;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/events")
@AllArgsConstructor
public class EventPublicController {
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> search(@RequestParam(value = "text", required = false)
                                                      String text,
                                                      @RequestParam(value = "categories", required = false)
                                                      List<Long> categories,
                                                      @RequestParam(value = "paid", required = false)
                                                      Boolean paid,
                                                      @RequestParam(value = "rangeStart", required = false)
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                      LocalDateTime rangeStart,
                                                      @RequestParam(value = "rangeEnd", required = false)
                                                          @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
                                                      LocalDateTime rangeEnd,
                                                      @RequestParam(value = "sort", required = false,
                                                              defaultValue = "EVENT_DATE") SortType sort,
                                                      @RequestParam(value = "onlyAvailable", required = false)
                                                      Boolean onlyAvailable,
                                                      @RequestParam(value = "from", defaultValue = "0")
                                                      @PositiveOrZero Long from,
                                                      @RequestParam(value = "size", defaultValue = "10")
                                                      @Positive Long size,
                                                      HttpServletRequest httpRequest) {
        SearchParam param = new SearchParam();
        param.setText(text);
        param.setCategories(categories);
        param.setPaid(paid);
        param.setRangeStart(rangeStart);
        param.setRangeEnd(rangeEnd);
        param.setOnlyAvailable(onlyAvailable);
        param.setSort(sort);

        return ResponseEntity.of(Optional.of(eventService.searchPublic(param, from, size, httpRequest)));
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getById(@PathVariable("eventId") @Positive Long eventId,
                                                HttpServletRequest httpRequest) {
        return ResponseEntity.of(Optional.of(eventService.getById(eventId, httpRequest)));
    }
}