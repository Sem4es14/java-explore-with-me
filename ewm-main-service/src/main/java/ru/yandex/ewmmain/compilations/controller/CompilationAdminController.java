package ru.yandex.ewmmain.compilations.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.compilations.requestdto.CompilationCreateRequest;
import ru.yandex.ewmmain.compilations.responsedto.CompilationDto;
import ru.yandex.ewmmain.compilations.service.CompilationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@RequestMapping("/admin/compilations")
@AllArgsConstructor
public class CompilationAdminController {
    private final CompilationService compilationService;

    @PostMapping
    public ResponseEntity<CompilationDto> createCompilation(@RequestBody @Valid CompilationCreateRequest request) {
        return ResponseEntity.of(Optional.of(compilationService.create(request)));
    }

    @DeleteMapping("/{compilationId}")
    public void deleteCompilation(@PathVariable("compilationId") @Positive Long compilationId) {
        compilationService.delete(compilationId);
    }

    @DeleteMapping("/{compilationId}/events/{eventId}")
    public void deleteEventFromCompilation(@PathVariable("compilationId") @Positive Long compilationId,
                                           @PathVariable("eventId") @Positive Long eventId) {
        compilationService.deleteEventFromCompilation(compilationId, eventId);
    }

    @PatchMapping("/{compilationId}/events/{eventId}")
    public ResponseEntity<CompilationDto> addEventToCompilation(@PathVariable("compilationId") @Positive
                                                                Long compilationId,
                                                                @PathVariable("eventId") @Positive Long eventId) {
        return ResponseEntity.of(Optional.of(compilationService.addEventToCompilation(compilationId, eventId)));
    }

    @DeleteMapping("/{compilationId}/pin")
    public void unpinCompilation(@PathVariable("compilationId") @Positive Long compilationId) {
        compilationService.unpin(compilationId);
    }

    @PatchMapping("/{compilationId}/pin")
    public ResponseEntity<CompilationDto> pinCompilation(@PathVariable("compilationId") @Positive Long compilationId) {
        return ResponseEntity.of(Optional.of(compilationService.pin(compilationId)));
    }
}