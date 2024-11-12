package ru.yandex.service.privateapi.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.yandex.dto.comment.CommentDto;
import ru.yandex.dto.comment.UpdateCommentDto;

import java.util.List;

public interface PrivateCommentService {

    CommentDto addComment(Long userId, Integer eventId, UpdateCommentDto dto, HttpServletRequest request);

    List<CommentDto> getCommentsByEvent(Long userId, Integer eventId, Boolean sortByCreated, Boolean sortByAuthor);

    CommentDto updateComment(Long userId, Integer eventId, Integer commentId, UpdateCommentDto newComment);

    void deleteComment(Long userId, Integer eventId, Integer commentId);

}
