package ru.yandex.ewmmain.comments.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.ewmmain.comments.model.Comment;
import ru.yandex.ewmmain.event.model.Event;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByEvent(Event event);

    List<Comment> findByEventIn(List<Event> events);
}
