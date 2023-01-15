package ru.yandex.ewmmain.comments.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.ewmmain.comments.mapper.CommentMapper;
import ru.yandex.ewmmain.comments.model.Comment;
import ru.yandex.ewmmain.comments.repository.CommentRepository;
import ru.yandex.ewmmain.comments.requestdto.CommentCreateRequest;
import ru.yandex.ewmmain.comments.requestdto.CommentUpdateRequest;
import ru.yandex.ewmmain.comments.responsedto.CommentDto;
import ru.yandex.ewmmain.event.model.Event;
import ru.yandex.ewmmain.event.repository.EventRepository;
import ru.yandex.ewmmain.exception.model.ForbiddenException;
import ru.yandex.ewmmain.exception.model.NotFoundException;
import ru.yandex.ewmmain.user.model.User;
import ru.yandex.ewmmain.user.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@AllArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public CommentDto create(CommentCreateRequest request, Long userId, Long eventId) {
        Event event = eventRepository.findById(eventId).orElseThrow(() -> {
            throw new NotFoundException("Event with id: " + eventId + " is not found");
        });
        User author = userRepository.findById(userId).orElseThrow(() -> {
            throw new NotFoundException("User with id: " + userId + " is not found");
        });

        Comment comment = new Comment();
        comment.setText(request.getText());
        comment.setCreated(LocalDateTime.now());
        comment.setEvent(event);
        comment.setAuthor(author);

        return CommentMapper.fromCommentToDto(commentRepository.save(comment));
    }

    public CommentDto updateByAdmin(CommentUpdateRequest request) {
        Comment comment = commentRepository.findById(request.getId()).orElseThrow(() -> {
            throw new NotFoundException("Comment with id: " + request.getId() + " is not found");
        });
        comment.setText(request.getText());
        comment.setEdited(LocalDateTime.now());

        return CommentMapper.fromCommentToDto(commentRepository.save(comment));
    }

    public CommentDto updateByUser(CommentUpdateRequest request, Long userId) {
        Comment comment = commentRepository.findById(request.getId()).orElseThrow(() -> {
            throw new NotFoundException("Comment with id: " + request.getId() + " is not found");
        });
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("User with id: " + userId + " can not update this comment");
        }
        comment.setText(request.getText());
        comment.setEdited(LocalDateTime.now());

        return CommentMapper.fromCommentToDto(commentRepository.save(comment));
    }

    public void deleteByUser(Long userId, Long commentId) {
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> {
            throw new NotFoundException("Comment with id: " + commentId + " is not found");
        });
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new ForbiddenException("User with id: " + userId + " can not delete this comment");
        }
        commentRepository.deleteById(commentId);
    }

    public void deleteByAdmin(Long commentId) {
        if (commentRepository.findById(commentId).isEmpty()) {
            throw new NotFoundException("Comment with id: " + commentId + " is not found");
        }
        commentRepository.deleteById(commentId);
    }
}
