package ru.yandex.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.yandex.error.apierror.exceptions.ConflictException;
import ru.yandex.error.apierror.exceptions.IncorrectParameterException;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.error.apierror.model.ApiError;

import java.time.LocalDateTime;

@RestControllerAdvice
public class ErrorHandler {
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError handleIncorrectParameterException(IncorrectParameterException e) {
        return new ApiError(
                "BAD_REQUEST",
                "Something wrong with request params!",
                e.getMessage(),
                LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError handleNotFoundException(NotFoundException e) {
        return new ApiError(
                "NOT_FOUND",
                "Entity with current params not found!",
                e.getMessage(),
                LocalDateTime.now().toString()
        );
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError handleNotFoundException(ConflictException e) {
        return new ApiError(
                "CONFLICT",
                "Entity with current params not found!",
                e.getMessage(),
                LocalDateTime.now().toString()
        );
    }
}