package ru.yandex.service.privateapi.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.error.apierror.exceptions.ConflictException;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.model.event.Event;
import ru.yandex.model.event.EventState;
import ru.yandex.model.event.EventStatus;
import ru.yandex.model.request.Request;
import ru.yandex.model.user.User;
import ru.yandex.repository.EventRepository;
import ru.yandex.repository.RequestRepository;
import ru.yandex.repository.UserRepository;

import java.util.List;

import static ru.yandex.mapper.RequestMapper.toRequest;

@Slf4j
@RequiredArgsConstructor
@Service
public class PrivateUserServiceImpl implements PrivateUserService {

    private final RequestRepository repository;
    private final EventRepository eventsRepository;
    private final UserRepository usersRepository;

    @Override
    public List<Request> getRequestsByUserId(int userId) {
        List<Request> requests = repository.findAllByRequesterId(userId);

        if (requests.isEmpty()) {
            throw new NotFoundException("User not found with id = " + userId);
        }

        log.info("Getting requests list for User id=" + userId);

        return requests;
    }

    @Override
    public Request postRequestsByUserId(int userId, int eventId) {
        Request request;

        Event event = eventsRepository.findById((long) eventId).orElseThrow(()
                -> new NotFoundException("Event not found with id = " + eventId));

        User user = usersRepository.findById((long) userId).orElseThrow(()
                -> new NotFoundException("User not found with id = " + userId));

        Integer confirmedRequests = event.getConfirmedRequests();

        if (repository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException("Request with requesterId= " + userId + " and eventId=" + eventId + " already exist");
        }
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException("User with id=" + userId + " must not be equal to initiator");
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Event with id=" + eventId + " is not published");
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException("Event with id=" + eventId + " has reached participant limit");
        }
        if (!event.getRequestModeration()) {
            if (confirmedRequests == null) {
                confirmedRequests = 0;
            }
            event.setConfirmedRequests(confirmedRequests + 1);
            event = eventsRepository.save(event);
        }
        request = repository.save(toRequest(user, event));

        if (event.getParticipantLimit() == 0) {
            request.setStatus(EventStatus.CONFIRMED);
        }
        log.info("Added request for User id=" + userId + " and Event id=" + eventId);

        return repository.save(request);
    }

    @Override
    public Request updateRequestsByUserId(int userId, int requestId) {
        Request request = repository.findByIdAndRequesterId(requestId, userId);

        if (request == null) {
            throw new ConflictException("Request with id= " + requestId + " and requesterId= "
                    + userId + " was not found");
        }
        log.info("Updating Request id=" + requestId);

        request.setStatus(EventStatus.CANCELED);

        return repository.save(request);
    }

}
