package ru.yandex.ewmmain.event.mapper;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.ewmmain.category.responsedto.CategoryDto;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.event.responsedto.EventFullDto;
import ru.yandex.ewmmain.event.responsedto.EventShortDto;
import ru.yandex.ewmmain.participationrequest.model.RequestStatus;
import ru.yandex.ewmmain.participationrequest.repository.RequestRepository;
import ru.yandex.ewmmain.user.responsedto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class EventMapper {
    private static RequestRepository requestRepository;
    public static EventFullDto fromEventToFullDto(Event event) {
        return new EventFullDto(
                event.getId(),
                event.getTitle(),
                event.getDescription(),
                event.getAnnotation(),
                event.getCreatedOn(),
                event.getEventDate(),
                event.getPublishedOn(),
                event.getPaid(),
                event.getState(),
                event.getRequestModeration(),
                event.getParticipantLimit(),
                new CategoryDto(
                        event.getCategory().getId(),
                        event.getCategory().getName()),
                new UserShortDto(
                        event.getInitiator().getId(),
                        event.getInitiator().getName()),
                null,
                requestRepository.countByEventAndStatus(event, RequestStatus.APPROVED),
                new EventFullDto.Location(
                        event.getLat(),
                        event.getLon())
        );
    }

    public static List<EventFullDto> fromEventsToFullDtos(List<Event> events) {
        return events.stream()
                .map(EventMapper::fromEventToFullDto)
                .collect(Collectors.toList());
    }

    public static EventShortDto fromEventToShortDto(Event event) {
        return new EventShortDto(
                event.getId(),
                event.getTitle(),
                event.getAnnotation(),
                event.getEventDate(),
                event.getPaid(),
                new CategoryDto(
                        event.getCategory().getId(),
                        event.getCategory().getName()),
                new UserShortDto(
                        event.getInitiator().getId(),
                        event.getInitiator().getName()),
                null,
                requestRepository.countByEventAndStatus(event, RequestStatus.APPROVED)
              );
    }

    public static List<EventShortDto> fromEventsToShortDtos(List<Event> events) {
        return events.stream()
                .map(EventMapper::fromEventToShortDto)
                .collect(Collectors.toList());
    }
}
