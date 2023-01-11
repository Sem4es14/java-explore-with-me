package ru.yandex.ewmmain.compilations.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.ewmmain.client.dto.StatsDto;
import ru.yandex.ewmmain.client.ewmclient.EwmClient;
import ru.yandex.ewmmain.compilations.mapper.CompilationsMapper;
import ru.yandex.ewmmain.compilations.model.Compilation;
import ru.yandex.ewmmain.compilations.repository.CompilationRepository;
import ru.yandex.ewmmain.compilations.requestdto.CompilationCreateRequest;
import ru.yandex.ewmmain.compilations.responsedto.CompilationDto;
import ru.yandex.ewmmain.event.mapper.EventMapper;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.event.repository.EventRepository;
import ru.yandex.ewmmain.event.responsedto.EventShortDto;
import ru.yandex.ewmmain.exception.model.NotFoundException;
import ru.yandex.ewmmain.participationrequest.model.ConfirmedRequests;
import ru.yandex.ewmmain.participationrequest.model.RequestStatus;
import ru.yandex.ewmmain.participationrequest.repository.RequestRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class CompilationService {
    private final CompilationRepository compilationRepository;
    private final EventRepository eventRepository;
    private final EwmClient ewmClient;
    private final RequestRepository requestRepository;

    public CompilationDto create(CompilationCreateRequest request) {
        List<Event> events = request.getEvents().stream()
                .map(eventRepository::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());
        Compilation compilation = new Compilation();
        compilation.setTitle(request.getTitle());
        compilation.setPinned(request.getPinned());
        compilation.setEvents(events);

        List<EventShortDto> eventShortDtos = events.stream()
                .map(event -> EventMapper.fromEventToShortDto(
                        event,
                        getViews(event),
                        getConfirmedRequestsMap(events).get(event.getId())
                ))
                .collect(Collectors.toList());

        return CompilationsMapper.fromCompilationToDto(compilationRepository.save(compilation), eventShortDtos);
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

        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map(event -> EventMapper.fromEventToShortDto(
                        event,
                        getViews(event),
                        getConfirmedRequestsMap(compilation.getEvents()).get(event.getId())
                ))
                .collect(Collectors.toList());

        return CompilationsMapper.fromCompilationToDto(compilation, eventShortDtos);
    }

    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations = compilationRepository.findByPinned(pinned, PageRequest.of(from / size, size));
        List<CompilationDto> compilationDtos = new ArrayList<>();
        for (Compilation compilation : compilations) {
            List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                    .map(event -> EventMapper.fromEventToShortDto(
                            event,
                            getViews(event),
                            getConfirmedRequestsMap(compilation.getEvents()).get(event.getId())
                    ))
                    .collect(Collectors.toList());
            compilationDtos.add(CompilationsMapper.fromCompilationToDto(compilation, eventShortDtos));
        }

        return compilationDtos;
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

        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map(e -> EventMapper.fromEventToShortDto(
                        e,
                        getViews(e),
                        getConfirmedRequestsMap(compilation.getEvents()).get(e.getId())
                ))
                .collect(Collectors.toList());

        return CompilationsMapper.fromCompilationToDto(compilationRepository.save(compilation), eventShortDtos);
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

        List<EventShortDto> eventShortDtos = compilation.getEvents().stream()
                .map(event -> EventMapper.fromEventToShortDto(
                        event,
                        getViews(event),
                        getConfirmedRequestsMap(compilation.getEvents()).get(event.getId())
                ))
                .collect(Collectors.toList());

        return CompilationsMapper.fromCompilationToDto(compilationRepository.save(compilation), eventShortDtos);
    }

    private Long getViews(Event event) {
        List<StatsDto> statsDtos = ewmClient.get(List.of("/events/" + event.getId()));
        if (statsDtos.size() == 0) {
            return 0L;
        }
        return statsDtos.get(0).getHits();
    }

    private Long getConfirmedRequests(Event event) {
        return requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED);
    }

    private Map<Long, Long> getConfirmedRequestsMap(List<Event> events) {
        Long[] eventIds = events.stream()
                .map(Event::getId)
                .toArray(Long[]::new);

        Map<Long, Long> confirmedRequestsMap = new HashMap<>();

        for (ConfirmedRequests confirmedRequests : requestRepository.countConfirmedRequests(eventIds)) {
            confirmedRequestsMap.put(confirmedRequests.getEventId(), confirmedRequests.getCount());
        }

        return confirmedRequestsMap;
    }
}
