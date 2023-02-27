package ru.yandex.ewmmain.compilations.controller;


import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.compilations.responsedto.CompilationDto;
import ru.yandex.ewmmain.compilations.service.CompilationService;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/compilations")
@AllArgsConstructor
public class CompilationPublicController {
    private final CompilationService compilationService;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getAll(@RequestParam(value = "pinned", defaultValue = "false")
                                                       @NotNull  Boolean pinned,
                                                       @RequestParam(value = "from", defaultValue = "0")
                                                       @PositiveOrZero Integer from,
                                                       @RequestParam(value = "size", defaultValue = "10")
                                                       @Positive Integer size) {
        return ResponseEntity.of(Optional.of(compilationService.getAll(pinned, from, size)));
    }

    @GetMapping("/{compilationId}")
    public ResponseEntity<CompilationDto> getById(@PathVariable("compilationId") @Positive Long compilationId) {
        return ResponseEntity.of(Optional.of(compilationService.getById(compilationId)));
    }
}