package ru.yandex.ewmmain.compilations.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.ewmmain.compilations.mapper.CompilationsMapper;
import ru.yandex.ewmmain.compilations.model.Compilation;
import ru.yandex.ewmmain.compilations.repository.CompilationRepository;
import ru.yandex.ewmmain.compilations.requestdto.CompilationCreateRequest;
import ru.yandex.ewmmain.compilations.responsedto.CompilationDto;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.event.repository.EventRepository;
import ru.yandex.ewmmain.exception.model.NotFoundException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;

    public CompilationDto create(CompilationCreateRequest request) {
        List<Event> events = request.getEvents().stream().map(eventRepository::findById).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toList());
        Compilation compilation = new Compilation();
        compilation.setTitle(request.getTitle());
        compilation.setPinned(request.getPinned());
        compilation.setEvents(events);

        return CompilationsMapper.fromCompilationToDto(compilationRepository.save(compilation));
    }

    public void delete(Long compilationId) {
        if (compilationRepository.findById(compilationId).isEmpty()) {
            throw new NotFoundException("Compilation with id " + compilationId + " is not found");
        }

        compilationRepository.deleteById(compilationId);
    }

    public CompilationDto getById(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() -> {
            throw new NotFoundException("Compilation with id: " + compilationId + " is not found");
        });

        return CompilationsMapper.fromCompilationToDto(compilation);
    }

    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {

        return CompilationsMapper.fromCompilationsToDtos(compilationRepository.findByPinned(pinned, PageRequest.of(from / size, size)));
    }

    public void deleteEventFromCompilation(Long compilationId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() -> {
            throw new NotFoundException("Compilation with id: " + compilationId + " is not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " is not found");
        });
        compilation.getEvents().remove(event);
        compilationRepository.save(compilation);
    }

    public CompilationDto addEventToCompilation(Long compilationId, Long eventId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() -> {
            throw new NotFoundException("Compilation with id: " + compilationId + " is not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " is not found");
        });
        compilation.getEvents().add(event);

        return CompilationsMapper.fromCompilationToDto(compilationRepository.save(compilation));
    }

    public void unpin(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() -> {
            throw new NotFoundException("Compilation with id: " + compilationId + " is not found");
        });
        compilation.setPinned(false);
        compilationRepository.save(compilation);

    }

    public CompilationDto pin(Long compilationId) {
        Compilation compilation = compilationRepository.findById(compilationId).orElseThrow(() -> {
            throw new NotFoundException("Compilation with id: " + compilationId + " is not found");
        });
        compilation.setPinned(true);

        return CompilationsMapper.fromCompilationToDto(compilationRepository.save(compilation));
    }
}
