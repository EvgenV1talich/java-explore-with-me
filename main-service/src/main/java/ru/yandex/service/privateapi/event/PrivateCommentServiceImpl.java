package ru.yandex.service.privateapi.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.dto.comment.CommentDto;
import ru.yandex.dto.comment.UpdateCommentDto;
import ru.yandex.error.apierror.exceptions.ConflictException;
import ru.yandex.error.apierror.exceptions.NoAccessException;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.mapper.CommentMapper;
import ru.yandex.model.comment.Comment;
import ru.yandex.model.event.Event;
import ru.yandex.model.user.User;
import ru.yandex.repository.CommentRepository;
import ru.yandex.service.adminapi.user.AdminUserService;
import ru.yandex.service.publicapi.event.PublicEventService;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PrivateCommentServiceImpl implements PrivateCommentService {

    private final CommentRepository repository;
    private final CommentMapper mapper;
    private final AdminUserService userService;
    private final PublicEventService eventService;

    @Override
    public CommentDto addComment(Long userId, Integer eventId, UpdateCommentDto dto, HttpServletRequest request) {

        if (!userService.exists(userId) || !eventService.exists(Long.valueOf(eventId))) {
            throw new NotFoundException("Entity with current params not found");
        }
        dto.setAuthorId(userId);
        User commentAuthor = userService.getUser(userId);
        //Для отладки принудительно ставим статус события как опубликованное
        Event event = eventService.setPublishedStateToEvent(eventId);
        Comment comment = mapper.updateCommentDtotoComment(dto, commentAuthor, event);
        return mapper.toDto(repository.save(comment));
    }

    @Override
    public List<CommentDto> getCommentsByEvent(Long userId, Integer eventId, Boolean sortByCreated, Boolean sortByAuthor) {
        if (sortByCreated == null || sortByAuthor == null) {
            return repository.findByEventId(eventId)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
        if (sortByAuthor && sortByCreated) {
            return repository.findByEventIdOrderByCreatedAtDescAuthorNameDesc(eventId)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
        if (sortByAuthor) {
            return repository.findByEventIdOrderByAuthorNameDesc(eventId)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
        if (sortByCreated) {
            return repository.findByEventIdOrderByCreatedAtDesc(eventId)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
        throw new NotFoundException("Something wrong when updating comment");
    }

    @Override
    public CommentDto updateComment(Long userId, Integer eventId, Integer commentId, UpdateCommentDto newComment) {
        Comment comment = repository.findById(commentId).orElseThrow(()
                -> new NotFoundException(MessageFormat.format("Comment with id + {0}not found", commentId)));
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NoAccessException(MessageFormat
                    .format("Invalid userId or eventId when updating comment! UserId: {0}, EventId: {1}",
                            userId,
                            eventId));
        }

        if (comment.getText().equals(newComment.getText())) {
            throw new ConflictException(MessageFormat
                    .format("Trying to change nothing... Before:{0} After:{1}",
                            comment.getText(),
                            newComment.getText()));
        }
        comment.setText(newComment.getText());
        comment.setChangedAt(LocalDateTime.now());
        return mapper.toDto(repository.save(comment));
    }

    @Override
    public void deleteComment(Long userId, Integer eventId, Integer commentId) {
        Comment comment = repository.getReferenceById(commentId);
        if (!comment.getAuthor().getId().equals(userId)) {
            throw new NoAccessException(MessageFormat
                    .format("This User (id = {0}) have no access to delete comment (id = {1})",
                            userId,
                            commentId));
        }
        if (!eventService.exists(Long.valueOf(eventId))) {
            throw new NotFoundException(MessageFormat
                    .format("Event with id {0} not found", eventId));
        }

        repository.deleteById(commentId);

    }
}
