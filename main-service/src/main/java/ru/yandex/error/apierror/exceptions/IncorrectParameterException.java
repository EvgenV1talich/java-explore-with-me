package ru.yandex.error.apierror.exceptions;

public class IncorrectParameterException extends RuntimeException {
    public IncorrectParameterException(final String message) {
        super(message);
    }

}
