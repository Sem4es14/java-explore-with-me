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
import java.time.temporal.ChronoUnit;
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
        if (requestRepository.countByEventAndStatus(event, CONFIRMED) >= event.getParticipantLimit()) {
            throw new ForbiddenException("Event with id" + eventId + "reached participant limit");
        }
        Request request = new Request();
        request.setStatus(event.getRequestModeration() ? PENDING : CONFIRMED);
        request.setCreated(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
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
        request.setStatus(CANCELED);

        return RequestMapper.fromRequestToDto(requestRepository.save(request));
    }

    public List<RequestDto> getRequestsByEvent(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " is not found");
        });
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found");
        });
        if (!event.getInitiator().equals(user)) {
            throw new ForbiddenException("User with id " + userId + " is not event initiator");
        }

        return RequestMapper.fromRequestsToDtos(requestRepository.findByEvent(event));
    }

    public RequestDto approveRequest(Long userId, Long eventId, Long requestId) {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id " + eventId + " is not found");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User with id " + userId + " is not found");
        }
        Request request = requestRepository.findById(requestId).orElseThrow(() -> {
            throw new NotFoundException("Request with id: " + requestId + " is not found");
        });
        request.setStatus(CONFIRMED);

        return RequestMapper.fromRequestToDto(requestRepository.save(request));
    }

    public RequestDto rejectRequest(Long userId, Long eventId, Long requestId) {
        if (eventRepository.findById(eventId).isEmpty()) {
            throw new NotFoundException("Event with id " + eventId + " is not found");
        }
        if (userRepository.findById(userId).isEmpty()) {
            throw new NotFoundException("User with id " + userId + " is not found");
        }
        Request request = requestRepository.findById(requestId).orElseThrow(() -> {
            throw new NotFoundException("Request with id: " + requestId + " is not found");
        });
        request.setStatus(REJECTED);

        return RequestMapper.fromRequestToDto(requestRepository.save(request));
    }
}