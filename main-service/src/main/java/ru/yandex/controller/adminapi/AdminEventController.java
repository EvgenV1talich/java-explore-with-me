package ru.yandex.controller.adminapi;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.dto.event.EventFullDto;
import ru.yandex.mapper.EventMapper;
import ru.yandex.model.event.Event;
import ru.yandex.model.event.SearchPublicEventsArgs;
import ru.yandex.model.event.UpdateEventAdminRequest;
import ru.yandex.service.adminapi.event.AdminEventService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/admin/events")
public class AdminEventController {

    private final AdminEventService service;
    private final EventMapper mapper;

    @GetMapping
    public ResponseEntity<List<EventFullDto>> getAll(@RequestParam(required = false)
                                                         LocalDateTime rangeStart,
                                                     @RequestParam(required = false)
                                                     LocalDateTime rangeEnd,
                                                     @RequestParam(required = false) List<Long> users,
                                                     @RequestParam(required = false) List<String> states,
                                                     @RequestParam(required = false) List<Long> categories,
                                                     @RequestParam(defaultValue = "0") @PositiveOrZero Integer from,
                                                     @RequestParam(defaultValue = "10") @Positive Integer size) {

        log.info("Received /GET request to AdminEventController...");
        SearchPublicEventsArgs args = mapper.toSearchPublicEventsArgs(users, states, categories, rangeStart, rangeEnd);
        return new ResponseEntity<>(pagedResponse(service.getEvents(args), from, size), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventById(@PathVariable int eventId,
                                                        @RequestBody @Valid UpdateEventAdminRequest
                                                                updateEventAdminRequest) {

        log.info("Received /PATCH request to AdminEventController...");
        return new ResponseEntity<>(mapper.toEventFullDto(service.updateEventById(eventId, updateEventAdminRequest)),
                HttpStatus.OK);
    }

    private List<EventFullDto> pagedResponse(List<Event> events, int from, int size) {
        List<EventFullDto> pagedEvents = new ArrayList<>();
        int totalEvents = events.size();
        int toIndex = from + size;

        if (from <= totalEvents) {
            if (toIndex > totalEvents) {
                toIndex = totalEvents;
            }
            for (Event event : events.subList(from, toIndex)) {
                pagedEvents.add(mapper.toEventFullDto(event));
            }
            return pagedEvents;
        } else {
            return Collections.emptyList();
        }
    }
}
