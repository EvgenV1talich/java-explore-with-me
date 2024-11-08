package ru.yandex.hit.exceptions;

import lombok.Getter;

@Getter
public class Response {
    private final String error;

    public Response(String error) {
        this.error = error;
    }

}
