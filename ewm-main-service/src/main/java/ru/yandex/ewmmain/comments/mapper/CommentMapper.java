package ru.yandex.ewmmain.comments.mapper;

import ru.yandex.ewmmain.comments.model.Comment;
import ru.yandex.ewmmain.comments.responsedto.CommentDto;

import java.util.List;
import java.util.stream.Collectors;

public class CommentMapper {
    public static CommentDto fromCommentToDto(Comment comment) {
        return new CommentDto(
                comment.getId(),
                comment.getText(),
                comment.getCreated(),
                comment.getEdited(),
                comment.getAuthor().getId(),
                comment.getEvent().getId()
        );
    }

    public static List<CommentDto> fromCommentsToDtos(List<Comment> comments) {
        return comments.stream()
                .map(CommentMapper::fromCommentToDto)
                .collect(Collectors.toList());
    }
}