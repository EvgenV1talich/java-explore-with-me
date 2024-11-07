package ru.yandex.error.apierror.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {
    private String status;
    private String reason;
    private String message;
    private String timestamp;
}
