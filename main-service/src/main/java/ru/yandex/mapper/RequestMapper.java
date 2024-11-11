package ru.yandex.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.dto.ParticipationRequestDto;
import ru.yandex.model.event.Event;
import ru.yandex.model.request.Request;
import ru.yandex.model.user.User;

import java.time.LocalDateTime;

import static ru.yandex.model.event.EventStatus.CONFIRMED;
import static ru.yandex.model.event.EventStatus.PENDING;

@Component
@RequiredArgsConstructor
public class RequestMapper {

    public static ParticipationRequestDto toParticipationRequestDto(Request request) {
        return new ParticipationRequestDto(
                request.getCreatedOn(),
                request.getEvent().getId(),
                request.getId(),
                request.getRequester().getId(),
                request.getStatus()
        );
    }

    public static Request toRequest(User user, Event event) {
        return new Request(
                null,
                event,
                user,
                LocalDateTime.now(),
                event.getRequestModeration() ? PENDING : CONFIRMED
        );
    }

}
