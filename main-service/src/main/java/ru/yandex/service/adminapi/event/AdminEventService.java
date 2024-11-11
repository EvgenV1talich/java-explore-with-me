package ru.yandex.service.adminapi.event;

import ru.yandex.model.event.Event;
import ru.yandex.model.event.SearchPublicEventsArgs;
import ru.yandex.model.event.UpdateEventAdminRequest;

import java.util.List;

public interface AdminEventService {

    List<Event> getEvents(SearchPublicEventsArgs searchPublicEventsArgs);

    Event updateEventById(int eventId, UpdateEventAdminRequest updateEventAdminRequest);

}
