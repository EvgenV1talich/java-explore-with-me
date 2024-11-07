package ru.yandex.mapper;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.dto.event.EventFullDto;
import ru.yandex.dto.event.EventShortDto;
import ru.yandex.dto.event.LocationDto;
import ru.yandex.dto.event.NewEventDto;
import ru.yandex.error.apierror.exceptions.IncorrectParameterException;
import ru.yandex.model.category.Category;
import ru.yandex.model.event.Event;
import ru.yandex.model.event.EventState;
import ru.yandex.model.event.Location;
import ru.yandex.model.event.SearchEventsArgs;
import ru.yandex.model.event.SearchPublicEventsArgs;
import ru.yandex.model.user.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.yandex.mapper.UserMapper.toUserShortDto;

@Component
@RequiredArgsConstructor
public class EventMapper {

    private final CategoryMapper categoryMapper;

    public EventShortDto toEventShortDto(Event event) {

        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                null,
                event.getDate(),
                toUserShortDto(event.getInitiator()),
                event.getPaid(),
                event.getTitle(),
                event.getViews()
        );
    }

    public Event toEventFromNewEventDto(NewEventDto newEventDto, Category category, User initiator,
                                        Location location) {
        return new Event(
                null,
                newEventDto.getAnnotation(),
                category,
                0,
                LocalDateTime.now(),
                newEventDto.getDescription(),
                newEventDto.getEventDate(),
                initiator,
                location,
                newEventDto.getPaid(),
                newEventDto.getParticipantLimit(),
                LocalDateTime.now(),
                newEventDto.getRequestModeration(),
                EventState.PENDING,
                newEventDto.getTitle(),
                0
        );
    }

    public EventFullDto toEventFullDto(Event event) {

        return new EventFullDto(
                event.getAnnotation(),
                categoryMapper.toCategoryDto(event.getCategory()),
                event.getConfirmedRequests(),
                event.getCreatedOn(),
                event.getDescription(),
                event.getDate(),
                event.getId(),
                toUserShortDto(event.getInitiator()),
                event.getLocation(),
                event.getPaid(),
                event.getParticipantLimit(),
                event.getPublishedOn(),
                event.getRequestModeration(),
                event.getState(),
                event.getTitle(),
                event.getViews()
        );
    }

    public List<EventShortDto> toListEventShortDtoFromListEvents(List<Event> events) {
        List<EventShortDto> eventsShortDto = new ArrayList<>();

        for (Event e : events) {
            eventsShortDto.add(toEventShortDto(e));
        }

        return eventsShortDto;
    }

    public Location toLocation(LocationDto locationDto) {

        return new Location(
                null,
                locationDto.getLat(),
                locationDto.getLon()
        );
    }

    public LocationDto toLocation(Location location) {

        return new LocationDto(
                location.getLat(),
                location.getLon()
        );
    }

    public SearchEventsArgs toSearchEventsArgs(LocalDateTime rangeStart, LocalDateTime rangeEnd, String text,
                                               List<Long> categories, Boolean paid, Boolean onlyAvailable,
                                               String sort, HttpServletRequest request) {

        if (rangeEnd != null && rangeEnd.isBefore(LocalDateTime.now())) {
            throw new IncorrectParameterException("Event must be published");
        }

        return new SearchEventsArgs(
                text,
                categories,
                paid,
                rangeStart,
                rangeEnd,
                onlyAvailable,
                sort,
                request
        );
    }

    public SearchPublicEventsArgs toSearchPublicEventsArgs(List<Long> users, List<String> states,
                                                           List<Long> categories, LocalDateTime rangeStart,
                                                           LocalDateTime rangeEnd) {
        return new SearchPublicEventsArgs(
                users,
                states,
                categories,
                rangeStart,
                rangeEnd
        );
    }

}
