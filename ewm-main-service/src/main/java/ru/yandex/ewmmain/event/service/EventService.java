package ru.yandex.ewmmain.event.service;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.yandex.ewmmain.category.model.Category;
import ru.yandex.ewmmain.category.repository.CategoryRepository;
import ru.yandex.ewmmain.client.dto.StatsDto;
import ru.yandex.ewmmain.client.ewmclient.EwmClient;
import ru.yandex.ewmmain.comments.model.Comment;
import ru.yandex.ewmmain.comments.repository.CommentRepository;
import ru.yandex.ewmmain.event.mapper.EventMapper;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.event.model.EventState;
import ru.yandex.ewmmain.event.repository.EventRepository;
import ru.yandex.ewmmain.event.requestdto.EventAdminUpdateRequest;
import ru.yandex.ewmmain.event.requestdto.EventCreateRequest;
import ru.yandex.ewmmain.event.requestdto.EventUpdateRequest;
import ru.yandex.ewmmain.event.requestdto.filterparam.SearchParam;
import ru.yandex.ewmmain.event.responsedto.EventFullDto;
import ru.yandex.ewmmain.event.responsedto.EventShortDto;
import ru.yandex.ewmmain.exception.model.ForbiddenException;
import ru.yandex.ewmmain.exception.model.NotFoundException;
import ru.yandex.ewmmain.participationrequest.model.ConfirmedRequests;
import ru.yandex.ewmmain.participationrequest.model.RequestStatus;
import ru.yandex.ewmmain.participationrequest.repository.RequestRepository;
import ru.yandex.ewmmain.user.model.User;
import ru.yandex.ewmmain.user.repository.UserRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class EventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final EwmClient ewmClient;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private  final CommentRepository commentRepository;

    public EventFullDto createEvent(Long userId, EventCreateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found");
        });
        Category category = categoryRepository.findById(request.getCategory()).orElseThrow(() -> {
            throw new NotFoundException("Category with id: " + request.getCategory() + " is not found");
        });
        Event event = new Event();
        event.setEventDate(request.getEventDate());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setCreatedOn(LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS));
        event.setAnnotation(request.getAnnotation());
        event.setDescription(request.getDescription());
        event.setInitiator(user);
        event.setPaid(request.getPaid());
        event.setState(EventState.PENDING);
        event.setLat(request.getLocation().getLat());
        event.setLon(request.getLocation().getLon());
        event.setTitle(request.getTitle());
        event.setCategory(category);
        event.setRequestModeration(request.getRequestModeration());

        return EventMapper.fromEventToFullDto(eventRepository.save(event), 0L, 0L, List.of());
    }

    public EventFullDto updateByUser(Long userId, EventUpdateRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found");
        });
        Event event = eventRepository.findById(request.getEventId()).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + userId + " is not found");
        });
        if (!event.getInitiator().equals(user)) {
            throw new ForbiddenException("Update event can not be given by this user");
        }
        Category category = categoryRepository.findById(request.getCategory()).orElseThrow(() -> {
            throw new NotFoundException("Category with id: " + request.getCategory() + " is not found");
        });
        event.setPaid(request.getPaid());
        event.setAnnotation(request.getAnnotation());
        event.setTitle(request.getTitle());
        event.setCategory(category);
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setParticipantLimit(request.getParticipantLimit());

        List<Comment> comments = commentRepository.findByEvent(event);

        return EventMapper.fromEventToFullDto(
                eventRepository.save(event),
                getViews(event),
                getConfirmedRequests(event),
                comments);
    }

    public EventFullDto updateByAdmin(Long eventId, EventAdminUpdateRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " is not found");
        });
        Category category = categoryRepository.findById(request.getCategory()).orElseThrow(() -> {
            throw new NotFoundException("Category with id: " + request.getCategory() + " is not found");
        });
        event.setAnnotation(request.getAnnotation());
        event.setCategory(category);
        event.setDescription(request.getDescription());
        event.setEventDate(request.getEventDate());
        event.setPaid(request.getPaid());
        event.setParticipantLimit(request.getParticipantLimit());
        event.setTitle(request.getTitle());

        List<Comment> comments = commentRepository.findByEvent(event);

        return EventMapper.fromEventToFullDto(
                eventRepository.save(event),
                getViews(event), getConfirmedRequests(event),
                comments);
    }

    public EventFullDto publish(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " is not found");
        });
        event.setPublishedOn(LocalDateTime.now());
        event.setState(EventState.PUBLISHED);

        List<Comment> comments = commentRepository.findByEvent(event);

        return EventMapper.fromEventToFullDto(
                eventRepository.save(event),
                getViews(event), getConfirmedRequests(event),
                comments);
    }

    public EventFullDto rejectByAdmin(Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " is not found");
        });
        event.setPublishedOn(null);
        event.setState(EventState.CANCELED);

        List<Comment> comments = commentRepository.findByEvent(event);

        return EventMapper.fromEventToFullDto(
                eventRepository.save(event),
                getViews(event), getConfirmedRequests(event),
                comments);
    }

    public EventFullDto getOfUserById(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " is not found");
        });
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found");
        });
        if (!event.getInitiator().equals(user)) {
            throw new ForbiddenException("User with id: " + userId + " can not give information about this event");
        }

        List<Comment> comments = commentRepository.findByEvent(event);

        return EventMapper.fromEventToFullDto(
                event,
                getViews(event), getConfirmedRequests(event),
                comments);
    }

    public EventFullDto rejectByUser(Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " is not found");
        });
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found");
        });
        if (!event.getInitiator().equals(user)) {
            throw new ForbiddenException("User with id: " + userId + " can not reject this event");
        }
        if (!event.getState().equals(EventState.PENDING)) {
            throw new ForbiddenException("Event must be PENDING state");
        }
        event.setState(EventState.CANCELED);

        List<Comment> comments = commentRepository.findByEvent(event);

        return EventMapper.fromEventToFullDto(
                event,
                getViews(event),
                getConfirmedRequests(event),
                comments);
    }

    public List<EventShortDto> searchPublic(SearchParam searchParam, Long from, Long size, HttpServletRequest httpRequest) {
        List<Event> events = eventRepository.findBySearchParam(searchParam);
        ewmClient.hit(httpRequest);
        return events.stream()
                .map(event -> EventMapper.fromEventToShortDto(
                        event,
                        getViews(event),
                        getConfirmedRequestsMap(events).get(event.getId())
                ))
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<EventFullDto> searchByAdmin(SearchParam searchParam, Long from, Long size) {
        List<Event> events = eventRepository.findBySearchParam(searchParam);
        Map<Long, List<Comment>> commentMap = getCommentsMap(events);

        return eventRepository.findBySearchParam(searchParam).stream()
                .map(event -> EventMapper.fromEventToFullDto(
                        event,
                        getViews(event),
                        getConfirmedRequestsMap(events).get(event.getId()),
                        commentMap.get(event.getId())
                ))
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public List<EventShortDto> getByUser(Long userId, Integer from, Integer size) {
        User user = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found");
        });
        List<Event> events = eventRepository.findByInitiator(user, PageRequest.of(from / size, size));

        return events.stream()
                .map(event -> EventMapper.fromEventToShortDto(
                        event,
                        getViews(event),
                        getConfirmedRequestsMap(events).get(event.getId())
                ))
                .skip(from)
                .limit(size)
                .collect(Collectors.toList());
    }

    public EventFullDto getById(Long eventId, HttpServletRequest httpRequest) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " is not found");
        });
        ewmClient.hit(httpRequest);
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ForbiddenException("This event not PUBLISHED yet");
        }

        List<Comment> comments = commentRepository.findByEvent(event);

        return EventMapper.fromEventToFullDto(
                event,
                getViews(event),
                getConfirmedRequests(event),
                comments);
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

    private Map<Long, List<Comment>> getCommentsMap(List<Event> events) {
        Map<Long, List<Comment>> commentMap = new HashMap<>();
        List<Comment> comments = commentRepository.findByEventIn(events);
        for (Event event : events) {
            commentMap.put(event.getId(), comments.stream()
                    .filter(comment -> comment.getEvent().equals(event))
                    .collect(Collectors.toList()));
        }

        return commentMap;
    }
}


