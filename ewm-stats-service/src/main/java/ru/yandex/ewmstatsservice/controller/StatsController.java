package ru.yandex.ewmstatsservice.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmstatsservice.requestdto.StatsRequest;
import ru.yandex.ewmstatsservice.responsedto.StatsResponse;
import ru.yandex.ewmstatsservice.service.StatsService;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
public class StatsController {
    private final StatsService statsService;

    @GetMapping("/stats")
    public ResponseEntity<List<StatsResponse>> getStats(@RequestParam(value = "start") String start,
                                                        @RequestParam(value = "end") String end,
                                                        @RequestParam(value = "uris") List<String> uris,
                                                        @RequestParam(value = "unique", defaultValue = "false")
                                                        Boolean unique) {
        return ResponseEntity.of(Optional.of(statsService.get(start, end, uris, unique)));
    }

    @PostMapping("/hit")
    public void saveStats(@RequestBody StatsRequest request) {
        statsService.hit(request);
    }
}