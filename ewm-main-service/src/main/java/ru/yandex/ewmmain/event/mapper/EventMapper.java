package ru.yandex.ewmmain.event.mapper;

import ru.yandex.ewmmain.category.responsedto.CategoryDto;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.event.responsedto.EventFullDto;
import ru.yandex.ewmmain.event.responsedto.EventShortDto;
import ru.yandex.ewmmain.user.responsedto.UserShortDto;

public class EventMapper {

    public static EventFullDto fromEventToFullDto(Event event, Long views, Long confirmedRequest) {
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
                views,
                confirmedRequest,
                new EventFullDto.Location(
                        event.getLat(),
                        event.getLon())
        );
    }


    public static EventShortDto fromEventToShortDto(Event event, Long views, Long confirmedRequest) {
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
                views,
                confirmedRequest
        );
    }
}
