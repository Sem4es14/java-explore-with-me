package ru.yandex.ewmmain.comments.controller;

import lombok.AllArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yandex.ewmmain.comments.requestdto.CommentUpdateRequest;
import ru.yandex.ewmmain.comments.responsedto.CommentDto;
import ru.yandex.ewmmain.comments.service.CommentService;


import javax.validation.Valid;
import javax.validation.constraints.Positive;
import java.util.Optional;

@RestController
@RequestMapping("/admin/comments")
@AllArgsConstructor
public class CommentsAdminController {
    private final CommentService commentService;

    @DeleteMapping("/{commentId}")
    public void deleteComment(@PathVariable("commentId") @Positive Long commentId) {
        commentService.deleteByAdmin(commentId);
    }

    @PatchMapping
    public ResponseEntity<CommentDto> updateComment(@RequestBody @Valid CommentUpdateRequest request) {
        return ResponseEntity.of(Optional.of(commentService.updateByAdmin(request)));
    }
}