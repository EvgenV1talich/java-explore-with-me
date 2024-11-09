package ru.yandex.service.adminapi.event;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.yandex.error.apierror.exceptions.ConflictException;
import ru.yandex.model.event.Event;
import ru.yandex.model.event.EventState;
import ru.yandex.model.event.EventStateAction;
import ru.yandex.model.event.SearchPublicEventsArgs;
import ru.yandex.model.event.UpdateEventAdminRequest;
import ru.yandex.repository.EventRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;

    @Override
    public List<Event> getEvents(SearchPublicEventsArgs searchPublicEventsArgs) {

        Specification<Event> spec = Specification.where((root, query, criteriaBuilder) -> null);

        if (searchPublicEventsArgs.getUsers() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("initiator").get("id").in(searchPublicEventsArgs.getUsers()));
        }
        if (searchPublicEventsArgs.getStates() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("state").as(String.class).in(searchPublicEventsArgs.getStates()));
        }
        if (searchPublicEventsArgs.getCategories() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(searchPublicEventsArgs.getCategories()));
        }
        if (searchPublicEventsArgs.getRangeStart() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("date"), searchPublicEventsArgs.getRangeStart()));
        }
        if (searchPublicEventsArgs.getRangeEnd() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("date"), searchPublicEventsArgs.getRangeEnd()));
        }

        log.info("Get events list...");

        return eventRepository.findAll(spec);
    }

    @Override
    @Transactional
    public Event updateEventById(int eventId, UpdateEventAdminRequest updateEventAdminRequest) {

        Event event = eventRepository.findById((long) eventId).orElseThrow();

        if (updateEventAdminRequest.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequest.getAnnotation());
        }
        if (updateEventAdminRequest.getDescription() != null) {
            event.setDescription(updateEventAdminRequest.getDescription());
        }
        if (updateEventAdminRequest.getTitle() != null) {
            event.setTitle(updateEventAdminRequest.getTitle());
        }
        if (updateEventAdminRequest.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
        }
        if (updateEventAdminRequest.getPaid() != null) {
            event.setPaid(updateEventAdminRequest.getPaid());
        }
        if (event.getState().equals(EventState.PUBLISHED)) {
            throw new ConflictException("Cannot publish the event because it's not in the right state: PUBLISHED");
        } else if (event.getState().equals(EventState.CANCELED)) {
            throw new ConflictException("Cannot publish the event because it's not in the right state: CANCELED");
        } else {
            if (updateEventAdminRequest.getStateAction() != null) {
                if (updateEventAdminRequest.getStateAction().toString().equals(EventStateAction.PUBLISH_EVENT.toString())) {
                    event.setState(EventState.PUBLISHED);
                }
                if (updateEventAdminRequest.getStateAction().toString().equals(EventStateAction.REJECT_EVENT.toString())) {
                    event.setState(EventState.CANCELED);
                }
            }
        }
        log.info("Event id= {} was updated!", eventId);

        return eventRepository.save(event);
    }

}
