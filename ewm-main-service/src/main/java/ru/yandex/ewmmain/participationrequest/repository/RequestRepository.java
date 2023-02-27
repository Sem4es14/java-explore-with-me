package ru.yandex.ewmmain.participationrequest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.participationrequest.model.ConfirmedRequests;
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

    @Query(value = "select r.event_id as event_id, count(r.request_id) as count from requests r " +
            "JOIN events e on e.event_id = r.event_id " +
            "where r.status = 'CONFIRMED' and " +
            "      r.event_id in (:eventIds) " +
            "group by r.event_id ", nativeQuery = true)
    List<ConfirmedRequests> countConfirmedRequests(Long[] eventIds);
}
