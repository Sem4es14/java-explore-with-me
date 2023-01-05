package ru.yandex.ewmmain.participationrequest.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.event.model.EventState;
import ru.yandex.ewmmain.event.repository.EventRepository;
import ru.yandex.ewmmain.exception.model.AlreadyExists;
import ru.yandex.ewmmain.exception.model.ForbiddenException;
import ru.yandex.ewmmain.exception.model.NotFoundException;
import ru.yandex.ewmmain.participationrequest.mapper.RequestMapper;
import ru.yandex.ewmmain.participationrequest.model.Request;
import ru.yandex.ewmmain.participationrequest.repository.RequestRepository;
import ru.yandex.ewmmain.participationrequest.responsedto.RequestDto;
import ru.yandex.ewmmain.user.model.User;
import ru.yandex.ewmmain.user.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;

import static ru.yandex.ewmmain.participationrequest.model.RequestStatus.*;

@Service
@AllArgsConstructor
public class RequestService {
    private final RequestRepository requestRepository;
    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    public RequestDto createRequest(Long userId, Long eventId) {
        User requester = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found");
        });
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + userId + " is not found");
        });
        if (requestRepository.findByRequesterAndEvent(requester, event).isPresent()) {
            throw new AlreadyExists("Request for this event and user is already exists");
        }
        if (event.getInitiator().equals(requester)) {
            throw new ForbiddenException("Requester can not be event initiator");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("Event must be PUBLISHED");
        }
        if (requestRepository.countByEventAndStatus(event, APPROVED) >= event.getParticipantLimit()) {
            throw new ForbiddenException("Event with id" + eventId + "reached participant limit");
        }
        Request request = new Request();
        request.setStatus(event.getRequestModeration() ? PENDING : APPROVED);
        request.setCreated(LocalDateTime.now());
        request.setRequester(requester);
        request.setEvent(event);

        return RequestMapper.fromRequestToDto(requestRepository.save(request));
    }

    public List<RequestDto> getByUser(Long userId) {
        User requester = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found");
        });

        return RequestMapper.fromRequestsToDtos(requestRepository.findByRequester(requester));
    }

    public RequestDto rejectRequestByUser(Long userId, Long requestId) {
        userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found");
        });
        Request request = requestRepository.findById(requestId).orElseThrow(() -> {
            throw new NotFoundException("Request with id: " + requestId + " is not found");
        });
        request.setStatus(REJECT);

        return RequestMapper.fromRequestToDto(requestRepository.save(request));
    }
}