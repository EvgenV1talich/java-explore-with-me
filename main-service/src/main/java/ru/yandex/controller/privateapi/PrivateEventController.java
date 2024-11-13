package ru.yandex.controller.privateapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.dto.ParticipationRequestDto;
import ru.yandex.dto.event.EventFullDto;
import ru.yandex.dto.event.EventRequestStatusUpdateRequest;
import ru.yandex.dto.event.EventRequestStatusUpdateResult;
import ru.yandex.dto.event.EventShortDto;
import ru.yandex.dto.event.NewEventDto;
import ru.yandex.dto.event.UpdateEventUserRequest;
import ru.yandex.mapper.EventMapper;
import ru.yandex.model.event.Event;
import ru.yandex.model.request.Request;
import ru.yandex.service.privateapi.event.PrivateEventService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ru.yandex.mapper.RequestMapper.toParticipationRequestDto;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/users/{userId}/events")
public class PrivateEventController {

    private final PrivateEventService service;
    private final EventMapper eventMapper;

    @GetMapping
    public ResponseEntity<List<EventShortDto>> getEventsByUser(@PathVariable int userId,
                                                               @RequestParam(defaultValue = "0") int from,
                                                               @RequestParam(defaultValue = "10") int size) {

        log.info("Received /GET request to PrivateEventController...");
        return new ResponseEntity<>(pagedResponse(service.getEventsByUser(userId), from, size), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<EventFullDto> addEvent(@PathVariable int userId,
                                                 @RequestBody @Valid NewEventDto newEventDto) {

        log.info("Received /POST request to PrivateEventController:\n%s"
                .formatted(newEventDto.toString()));
        EventFullDto eventFullDto = eventMapper.toEventFullDto(service.addEvent(userId, newEventDto));
        return new ResponseEntity<>(eventFullDto, HttpStatus.CREATED);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<EventFullDto> getEventById(@PathVariable int userId, @PathVariable int eventId) {

        log.info("Received /GET by id request to PrivateEventController...");
        return new ResponseEntity<>(eventMapper.toEventFullDto(service.getEventById(userId, eventId)), HttpStatus.OK);
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateEventById(@PathVariable int userId,
                                                        @PathVariable int eventId,
                                                        @RequestBody @Valid UpdateEventUserRequest updateEventUserRequest) {

        log.info("Received /UPDATE request to PrivateEventController:\n%s"
                .formatted(updateEventUserRequest.toString()));
        return new ResponseEntity<>(eventMapper.toEventFullDto(service.updateEventById(userId, eventId,
                updateEventUserRequest)), HttpStatus.OK);
    }

    @GetMapping("/{eventId}/requests")
    public ResponseEntity<List<ParticipationRequestDto>> getRequests(@PathVariable int userId,
                                                                     @PathVariable int eventId) {

        log.info("Received /GET-requests request to PrivateEventController...");
        List<Request> requests = service.getRequests(userId, eventId);
        List<ParticipationRequestDto> participationRequestsDto = new ArrayList<>();

        for (Request request : requests) {
            participationRequestsDto.add(toParticipationRequestDto(request));
        }

        return new ResponseEntity<>(participationRequestsDto, HttpStatus.OK);
    }

    @PatchMapping("/{eventId}/requests")
    public ResponseEntity<EventRequestStatusUpdateResult> updateRequests(@PathVariable int userId,
                                                                         @PathVariable int eventId,
                                                                         @RequestBody EventRequestStatusUpdateRequest
                                                                                 eventRequestStatusUpdateRequest) {

        log.info("Received /PATCH-requests request to PrivateEventController:\n%s"
                .formatted(eventRequestStatusUpdateRequest.toString()));
        return new ResponseEntity<>(service.updateRequests(userId, eventId, eventRequestStatusUpdateRequest),
                HttpStatus.OK);
    }

    private List<EventShortDto> pagedResponse(List<Event> events, int from, int size) {
        List<EventShortDto> pagedEvents = new ArrayList<>();
        int totalEvents = events.size();
        int toIndex = from + size;

        if (from <= totalEvents) {
            if (toIndex > totalEvents) {
                toIndex = totalEvents;
            }
            for (Event event : events.subList(from, toIndex)) {
                pagedEvents.add(eventMapper.toEventShortDto(event));
            }
            return pagedEvents;
        } else {
            return Collections.emptyList();
        }
    }

}
