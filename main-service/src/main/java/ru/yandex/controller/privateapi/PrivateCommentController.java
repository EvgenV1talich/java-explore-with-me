package ru.yandex.controller.privateapi;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.dto.comment.CommentDto;
import ru.yandex.dto.comment.UpdateCommentDto;
import ru.yandex.service.privateapi.event.PrivateCommentService;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateCommentController {

    private final PrivateCommentService service;

    @PostMapping("/{eventId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long userId, @PathVariable Integer eventId,
                                                    @RequestBody @Valid UpdateCommentDto newCommentDto,
                                                    HttpServletRequest request) {

        log.info("Received /POST request to CommentController:\n" + newCommentDto.toString());

        return new ResponseEntity<>(service.addComment(userId, eventId, newCommentDto, request),
                HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}/comment")
    public ResponseEntity<List<CommentDto>> getAllCommentsByEvent(@PathVariable Long userId,
                                                                  @PathVariable Integer eventId,
                                                                  @RequestParam(required = false)
                                                                  Boolean sortByCreated,
                                                                  @RequestParam(required = false)
                                                                  Boolean sortByAuthor) {

        log.info("Received /GET request to CommentController");

        return new ResponseEntity<>(service
                .getCommentsByEvent(userId, eventId, sortByCreated, sortByAuthor), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/comment/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long userId, @PathVariable Integer eventId,
                                                    @PathVariable Integer commentId,
                                                    @RequestBody UpdateCommentDto newCommentDto) {

        log.info("Received /PATCH request to CommentController:\n%s"
                .formatted(newCommentDto.toString()));

        return new ResponseEntity<>(service.updateComment(userId, eventId, commentId, newCommentDto),
                HttpStatus.OK);
    }

    @DeleteMapping("/{eventId}/comment/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long userId, @PathVariable Integer eventId,
                                              @PathVariable Integer commentId) {

        log.info("Received /DELETE request to CommentController: commentId = {}", commentId);

        service.deleteComment(userId, eventId, commentId);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
