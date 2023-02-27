package ru.yandex.ewmmain.event.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.event.repository.customqueryrepo.JdbcTemplateRepository;
import ru.yandex.ewmmain.user.model.User;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long>, JdbcTemplateRepository {
    List<Event> findByInitiator(User initiator, Pageable pageable);
}
