package ru.yandex.service.privateapi.event;

import ru.yandex.dto.event.EventRequestStatusUpdateRequest;
import ru.yandex.dto.event.EventRequestStatusUpdateResult;
import ru.yandex.dto.event.NewEventDto;
import ru.yandex.dto.event.UpdateEventUserRequest;
import ru.yandex.model.event.Event;
import ru.yandex.model.request.Request;

import java.util.List;

public interface PrivateEventService {

    List<Event> getEventsByUser(int userId);

    Event addEvent(int userId, NewEventDto newEventDto);

    Event getEventById(int userId, int eventId);

    Event updateEventById(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest);

    List<Request> getRequests(int userId, int eventId);

    EventRequestStatusUpdateResult updateRequests(int userId, int eventId, EventRequestStatusUpdateRequest request);

}
