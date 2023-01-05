package ru.yandex.ewmmain.event.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.ewmmain.event.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {

}
