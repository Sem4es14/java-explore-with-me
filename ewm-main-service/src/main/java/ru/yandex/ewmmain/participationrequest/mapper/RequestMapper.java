package ru.yandex.ewmmain.participationrequest.mapper;

import ru.yandex.ewmmain.participationrequest.model.Request;
import ru.yandex.ewmmain.participationrequest.responsedto.RequestDto;

import java.util.List;
import java.util.stream.Collectors;

public class RequestMapper {
    public static RequestDto fromRequestToDto(Request request) {
        return new RequestDto(
                request.getId(),
                request.getStatus(),
                request.getCreated(),
                request.getRequester().getId(),
                request.getEvent().getId()
        );
    }

    public static List<RequestDto> fromRequestsToDtos(List<Request> requests) {
        return requests.stream()
                .map(RequestMapper::fromRequestToDto)
                .collect(Collectors.toList());
    }
}