package ru.yandex.ewmmain.event.mapper;

import ru.yandex.ewmmain.category.responsedto.CategoryDto;
import ru.yandex.ewmmain.client.ewmclient.EwmClient;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.event.responsedto.EventFullDto;
import ru.yandex.ewmmain.event.responsedto.EventShortDto;
import ru.yandex.ewmmain.participationrequest.model.RequestStatus;
import ru.yandex.ewmmain.participationrequest.repository.RequestRepository;
import ru.yandex.ewmmain.user.responsedto.UserShortDto;

import java.util.List;
import java.util.stream.Collectors;

public class EventMapper {

    public static EventFullDto fromEventToFullDto(Event event, EwmClient ewmClient, RequestRepository requestRepository) {
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
                ewmClient.get(List.of("/events/" + event.getId())).get(0).getHits(),
                requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED),
                new EventFullDto.Location(
                        event.getLat(),
                        event.getLon())
        );
    }

    public static List<EventFullDto> fromEventsToFullDtos(List<Event> events, EwmClient ewmClient, RequestRepository requestRepository) {
        return events.stream()
                .map(event -> EventMapper.fromEventToFullDto(event, ewmClient, requestRepository))
                .collect(Collectors.toList());
    }

    public static EventShortDto fromEventToShortDto(Event event, EwmClient ewmClient, RequestRepository requestRepository) {
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
                ewmClient.get(List.of("/events/" + event.getId())).get(0).getHits(),
                requestRepository.countByEventAndStatus(event, RequestStatus.CONFIRMED)
        );
    }

    public static List<EventShortDto> fromEventsToShortDtos(List<Event> events, EwmClient ewmClient, RequestRepository requestRepository) {
        return events.stream()
                .map(event -> EventMapper.fromEventToShortDto(event, ewmClient, requestRepository))
                .collect(Collectors.toList());
    }
}
