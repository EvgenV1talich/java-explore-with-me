package ru.yandex.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.dto.comment.CommentDto;
import ru.yandex.model.comment.Comment;
import ru.yandex.model.user.User;

@Component
public class CommentMapper {

    public CommentDto toDto(Comment comment) {
        if (comment == null) {
            return null;
        }
        CommentDto dto = new CommentDto();
        dto.setId(comment.getId());
        dto.setAuthor(comment.getAuthor().getId());
        dto.setText(comment.getText());
        dto.setCreatedAt(comment.getCreatedAt());
        dto.setChangedAt(comment.getChangedAt());
        dto.setEvent(comment.getEvent());
        return dto;
    }

    public Comment toComment(CommentDto dto, User author) {
        if (dto == null) {
            return null;
        }
        Comment comment = new Comment();
        comment.setId(dto.getId());
        comment.setAuthor(author);
        comment.setText(dto.getText());
        comment.setCreatedAt(dto.getCreatedAt());
        comment.setChangedAt(dto.getChangedAt());
        comment.setEvent(dto.getEvent());
        return comment;
    }

}