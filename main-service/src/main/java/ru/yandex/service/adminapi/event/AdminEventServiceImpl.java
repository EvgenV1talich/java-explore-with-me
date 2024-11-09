package ru.yandex.service.adminapi.event;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.yandex.mapper.EventMapper;
import ru.yandex.model.event.Event;
import ru.yandex.model.event.SearchPublicEventsArgs;
import ru.yandex.model.event.UpdateEventAdminRequest;
import ru.yandex.repository.EventRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminEventServiceImpl implements AdminEventService {

    private final EventRepository eventRepository;
    private final EventMapper mapper;

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

        Event newEvent = mapper.toEventFromAdminUpdateRequest(event, updateEventAdminRequest);
        log.info("Event id= {} was updated!", eventId);

        return eventRepository.save(newEvent);
    }

}
