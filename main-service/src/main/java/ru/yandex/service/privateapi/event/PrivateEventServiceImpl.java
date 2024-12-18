package ru.yandex.service.privateapi.event;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import ru.yandex.dto.ParticipationRequestDto;
import ru.yandex.dto.event.EventRequestStatusUpdateRequest;
import ru.yandex.dto.event.EventRequestStatusUpdateResult;
import ru.yandex.dto.event.NewEventDto;
import ru.yandex.dto.event.UpdateEventUserRequest;
import ru.yandex.error.apierror.exceptions.ConflictException;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.mapper.EventMapper;
import ru.yandex.mapper.RequestMapper;
import ru.yandex.model.category.Category;
import ru.yandex.model.event.Event;
import ru.yandex.model.event.EventState;
import ru.yandex.model.event.Location;
import ru.yandex.model.event.StateAction;
import ru.yandex.model.request.Request;
import ru.yandex.model.user.User;
import ru.yandex.repository.CategoryRepository;
import ru.yandex.repository.EventRepository;
import ru.yandex.repository.LocationRepository;
import ru.yandex.repository.RequestRepository;
import ru.yandex.repository.UserRepository;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static ru.yandex.model.event.EventStatus.CONFIRMED;
import static ru.yandex.model.event.EventStatus.REJECTED;

@Service
@RequiredArgsConstructor
@Slf4j
public class PrivateEventServiceImpl implements PrivateEventService {

    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final RequestRepository requestRepository;
    private final LocationRepository locationRepository;
    private final EventMapper eventMapper;

    @Override
    public List<Event> getEventsByUser(int userId) {
        log.info("Getting events list for User id = {}", userId);

        return eventRepository.findAllByInitiatorId(userId);
    }

    @Override
    @Transactional
    public Event addEvent(int userId, NewEventDto newEventDto) {
        Event event;

        if (newEventDto.getPaid() == null) {
            newEventDto.setPaid(false);
        }
        if (newEventDto.getParticipantLimit() == null) {
            newEventDto.setParticipantLimit(0);
        }
        if (newEventDto.getRequestModeration() == null) {
            newEventDto.setRequestModeration(true);
        }

        Category category = categoryRepository.findById(Long.valueOf(newEventDto.getCategory())).orElseThrow(()
                -> new NotFoundException(MessageFormat
                .format("Category with id={0} was not found", newEventDto.getCategory())));

        User user = userRepository.findById((long) userId).orElseThrow(()
                -> new NotFoundException(MessageFormat
                .format("User with id={0} was not found", userId)));
        Location location = locationRepository.save(eventMapper.toLocation(newEventDto.getLocation()));
        try {
            event = eventRepository.save(eventMapper.toEventFromNewEventDto(newEventDto, category, user, location));
        } catch (DataIntegrityViolationException e) {
            throw new ConflictException(e.getCause() + "\nField: category. Error: must not be blank. Value: null");
        }

        log.info(MessageFormat
                .format("Added new event id = {0} for User id = {1}", event.getId(), userId));

        return event;
    }

    @Override
    public Event getEventById(int userId, int eventId) {
        log.info(MessageFormat
                .format("Search Event id={0} for User id={1}", eventId, userId));

        Event event = eventRepository.findById((long) eventId).orElseThrow(()
                -> new NotFoundException(MessageFormat
                .format("Event not found with id = {0} and userId = {1}", eventId, userId)));
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException(MessageFormat
                    .format("Event not found with id = {0} and userId = {1}", eventId, userId));
        }

        return event;
    }

    @Override
    public Event updateEventById(int userId, int eventId, UpdateEventUserRequest updateEventUserRequest) {

        Event event = eventRepository.findById((long) eventId).orElseThrow(()
                -> new NotFoundException("Event not found with id = %d and userId = %d".formatted(eventId, userId)));
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Event not found with id = %d and userId = %d".formatted(eventId, userId));
        }

        if (updateEventUserRequest.getCategory() != null) {
            event.setCategory(categoryRepository.findById((long) updateEventUserRequest.getCategory())
                    .orElseThrow(() -> new NotFoundException("Category with id=%d was not found"
                            .formatted(updateEventUserRequest.getCategory()))));
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event must not be published");
        }

        if (updateEventUserRequest.getStateAction() != null) {
            if (StateAction.CANCEL_REVIEW.toString().equals(updateEventUserRequest.getStateAction().toString())) {
                event.setState(EventState.CANCELED);
            } else if (StateAction.SEND_TO_REVIEW.toString()
                    .equals(updateEventUserRequest.getStateAction().toString())) {
                event.setState(EventState.PENDING);
            }
        }
        log.info("Updating Event id=%d for User id=%d".formatted(eventId, userId));

        return event;
    }

    @Override
    public List<Request> getRequests(int userId, int eventId) {

        List<Request> allRequests = requestRepository.findAll();
        List<Request> requests = new ArrayList<>();

        for (Request r : allRequests) {
            if (r.getEvent().getId() == eventId && r.getEvent().getInitiator().getId() == userId) {
                requests.add(r);
            }
        }

        if (requests.isEmpty()) {
            throw new NotFoundException("Event not found with id=%d and userId=%d".formatted(eventId, userId));
        }

        log.info("Getting requests list for Event id=%d for User id=%d".formatted(eventId, userId));

        return requests;
    }

    @Override
    @Transactional
    public EventRequestStatusUpdateResult updateRequests(int userId, int eventId,
                                                         EventRequestStatusUpdateRequest request) {

        log.info("Updating requests for Event id=%d for User id=%d".formatted(eventId, userId));

        String status = request.getStatus();
        List<ParticipationRequestDto> confirmedRequests = new ArrayList<>();
        List<ParticipationRequestDto> rejectedRequests = new ArrayList<>();

        List<Integer> requestIds = request.getRequestIds();
        List<Request> requests = requestRepository.findAllByIdIn(requestIds);

        if (status.equals(REJECTED.toString())) {
            if (status.equals(REJECTED.toString())) {
                boolean isConfirmedRequestExists = requests.stream()
                        .anyMatch(r -> r.getStatus().equals(CONFIRMED));
                if (isConfirmedRequestExists) {
                    throw new ConflictException("Cannot reject confirmed requests");
                }
                rejectedRequests = requests.stream()
                        .peek(r -> r.setStatus(REJECTED))
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
            }
        }

        Event event = eventRepository.findById((long) eventId).orElseThrow(() ->
                new NotFoundException("Event not found with id = %d and userId %d".formatted(eventId, userId)));
        if (event.getInitiator().getId() != userId) {
            throw new NotFoundException("Event not found with id = %d and userId %d".formatted(eventId, userId));
        }

        Integer participantLimit = event.getParticipantLimit();
        Integer approvedRequests = event.getConfirmedRequests();

        if (approvedRequests == null) {
            approvedRequests = 0;
        }
        int availableParticipants = participantLimit - approvedRequests;
        int potentialParticipants = requestIds.size();

        if (participantLimit > 0 && participantLimit.equals(approvedRequests)) {
            throw new ConflictException("Event with id=%d has reached participant limit".formatted(event.getId()));
        }

        if (status.equals(CONFIRMED.toString())) {
            if (potentialParticipants <= availableParticipants && !event.getRequestModeration()) {
                confirmedRequests = requests.stream()
                        .peek(r -> {
                            if (!r.getStatus().equals(CONFIRMED)) {
                                r.setStatus(CONFIRMED);
                            } else {
                                throw new ConflictException("Request with id=%d has already been confirmed"
                                        .formatted(r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(approvedRequests + potentialParticipants);
            } else {
                confirmedRequests = requests.stream()
                        .limit(availableParticipants)
                        .peek(r -> {
                            if (!r.getStatus().equals(CONFIRMED)) {
                                r.setStatus(CONFIRMED);
                            } else {
                                throw new ConflictException("Request with id=%d has already been confirmed"
                                        .formatted(r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                rejectedRequests = requests.stream()
                        .skip(availableParticipants)
                        .peek(r -> {
                            if (!r.getStatus().equals(REJECTED)) {
                                r.setStatus(REJECTED);
                            } else {
                                throw new ConflictException("Request with id=%d has already been rejected"
                                        .formatted(r.getId()));
                            }
                        })
                        .map(RequestMapper::toParticipationRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            }
        }
        eventRepository.save(event);

        return new EventRequestStatusUpdateResult(confirmedRequests, rejectedRequests);
    }

}
