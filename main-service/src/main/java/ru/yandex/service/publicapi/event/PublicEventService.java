package ru.yandex.service.publicapi.event;

import jakarta.servlet.http.HttpServletRequest;
import ru.yandex.model.event.Event;
import ru.yandex.model.event.SearchEventsArgs;

import java.util.List;

public interface PublicEventService {

    List<Event> getEvents(SearchEventsArgs args);

    Event getEventById(int eventId, HttpServletRequest request);

    Event setPublishedStateToEvent(int eventId);

    boolean exists(Long id);

}
