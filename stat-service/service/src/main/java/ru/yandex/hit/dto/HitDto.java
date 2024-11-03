package ru.yandex.hit.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * DTO for {@link ru.yandex.hit.model.Hit}
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HitDto implements Serializable {
    String app;
    String uri;
    LocalDateTime timestamp;
}