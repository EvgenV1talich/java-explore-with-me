package ru.yandex.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateCommentDto {

    private Long authorId;
    @NotBlank
    @Size(min = 1, max = 1000)
    private String text;

}
