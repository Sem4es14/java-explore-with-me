package ru.yandex.ewmmain.comments.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.comments.requestdto.CommentCreateRequest;
import ru.yandex.ewmmain.comments.requestdto.CommentUpdateRequest;
import ru.yandex.ewmmain.comments.responsedto.CommentDto;
import ru.yandex.ewmmain.comments.service.CommentService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@RequestMapping("/users/{userId}/comments")
@AllArgsConstructor
public class CommentsPrivateController {
    private final CommentService commentService;

    @PostMapping("/events/{eventId}")
    public ResponseEntity<CommentDto> createComment(@PathVariable("userId") @Positive Long userId,
                                                    @PathVariable("eventId") @Positive Long eventId,
                                                    @RequestBody CommentCreateRequest request) {
        return ResponseEntity.of(Optional.of(commentService.create(request, userId, eventId)));
    }

    @PatchMapping
    public ResponseEntity<CommentDto> updateComment(@PathVariable("userId") @Positive Long userId,
                                                    @RequestBody @Valid CommentUpdateRequest request) {
        return ResponseEntity.of(Optional.of(commentService.updateByUser(request, userId)));
    }

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable("userId") @Positive Long userId,
                              @PathVariable("commentId") @Positive Long commentId) {
        commentService.deleteByUser(userId, commentId);
    }
}
