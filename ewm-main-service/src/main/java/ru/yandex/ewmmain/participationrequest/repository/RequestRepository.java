package ru.yandex.ewmmain.participationrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.participationrequest.model.Request;
import ru.yandex.ewmmain.participationrequest.model.RequestStatus;
import ru.yandex.ewmmain.user.model.User;

import java.util.List;
import java.util.Optional;

public interface RequestRepository extends JpaRepository<Request, Long> {
    Optional<Request> findByRequesterAndEvent(User requester, Event event);

    Long countByEventAndStatus(Event event, RequestStatus status);

    List<Request> findByRequester(User requester);

    List<Request> findByEvent(Event event);
}
