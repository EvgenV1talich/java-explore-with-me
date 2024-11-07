package ru.yandex.error.apierror.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApiError {

    //TODO what type???
    private String message;
    private String reason;
    //FIXME string or enum???
    private String status;
    private LocalDateTime timestamp;

}
