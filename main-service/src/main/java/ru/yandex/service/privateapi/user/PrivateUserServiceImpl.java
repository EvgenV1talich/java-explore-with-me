package ru.yandex.service.privateapi.user;

import jakarta.transaction.Transactional;
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

import java.text.MessageFormat;
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
            throw new NotFoundException(MessageFormat
                    .format("User not found with id = {0}", userId));
        }

        log.info(MessageFormat
                .format("Getting requests list for User id={0}", userId));

        return requests;
    }

    @Override
    @Transactional
    public Request postRequestsByUserId(int userId, int eventId) {
        Request request;

        Event event = eventsRepository.findById((long) eventId).orElseThrow(()
                -> new NotFoundException(MessageFormat
                .format("Event not found with id = {0}", eventId)));

        User user = usersRepository.findById((long) userId).orElseThrow(()
                -> new NotFoundException(MessageFormat
                .format("User not found with id = {0}", userId)));

        Integer confirmedRequests = event.getConfirmedRequests();

        if (repository.existsByRequesterIdAndEventId(userId, eventId)) {
            throw new ConflictException(MessageFormat
                    .format("Request with requesterId= {0} and eventId={1} already exist", userId, eventId));
        }
        if (event.getInitiator().getId() == userId) {
            throw new ConflictException(MessageFormat
                    .format("User with id={0} must not be equal to initiator", userId));
        }
        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException(MessageFormat
                    .format("Event with id={0} is not published", eventId));
        }
        if (event.getParticipantLimit() != 0 && event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException(MessageFormat
                    .format("Event with id={0} has reached participant limit", eventId));
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
        log.info(MessageFormat
                .format("Added request for User id={0} and Event id={1}", userId, eventId));

        return repository.save(request);
    }

    @Override
    @Transactional
    public Request updateRequestsByUserId(int userId, int requestId) {
        Request request = repository.findByIdAndRequesterId(requestId, userId);

        if (request == null) {
            throw new ConflictException(MessageFormat
                    .format("Request with id= {0} and requesterId= {1} was not found", requestId, userId));
        }
        log.info(MessageFormat
                .format("Updating Request id={0}", requestId));

        request.setStatus(EventStatus.CANCELED);

        return repository.save(request);
    }

}
