package ru.yandex.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.yandex.model.event.Event;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {

    private Integer id;
    private Long author;
    @NotBlank
    @Size(min = 1, max = 1000)
    private String text;
    @PastOrPresent
    private LocalDateTime createdAt;
    @PastOrPresent
    private LocalDateTime changedAt;
    private Event event;


}
