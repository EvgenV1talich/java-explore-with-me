package ru.yandex.service.publicapi.event;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.model.event.Event;
import ru.yandex.model.event.EventState;
import ru.yandex.model.event.SearchEventsArgs;
import ru.yandex.repository.EventRepository;

import java.util.Comparator;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PublicEventServiceImpl implements PublicEventService {

    private final EventRepository eventsRepository;
    //private final StatisticsClient client;

    @Value("${ewm.service.name}")
    private String serviceName;

    @Override
    public List<Event> getEvents(SearchEventsArgs args) {

        Specification<Event> spec = Specification.where((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("state"), EventState.PUBLISHED));

        if (args.getText() != null) {
            String searchText = String.format("%%%s%%", args.getText());
            spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.or(
                    criteriaBuilder.like(root.get("annotation"), searchText),
                    criteriaBuilder.like(root.get("description"), searchText)
            ));
        }
        if (args.getCategories() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    root.get("category").get("id").in(args.getCategories()));
        }
        if (args.getPaid() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("paid"), args.getPaid()));
        }
        if (args.getRangeStart() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThan(root.get("date"), args.getRangeStart()));
        }
        if (args.getRangeEnd() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThan(root.get("date"), args.getRangeEnd()));
        }
        if (args.getOnlyAvailable() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                            criteriaBuilder.equal(root.get("participantLimit"), 0),
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("confirmedRequests"),
                                    root.get("participantLimit").as(Long.class)
                            )
                    )
            );
        }

        List<Event> events = eventsRepository.findAll(spec);

        if (args.getSort() != null) {
            switch (args.getSort()) {
                case "EVENT_DATE":
                    events.sort(Comparator.comparing(Event::getEventDate));
                    break;
                case "VIEWS":
                    events.sort(Comparator.comparing(Event::getViews));
                    break;
                default:
                    events.sort(Comparator.comparing(Event::getId));
            }
        }

        log.info("Сформирован список запросов согласно спецификации");

        saveHit(args.getRequest());

        return events;
    }

    @Override
    public Event getEventById(int eventId, HttpServletRequest request) {
        Event event = eventsRepository.findById(eventId).orElseThrow(()
                -> new NotFoundException("Event not found with id = " + eventId));

        if (!event.getState().equals(EventState.PUBLISHED)) {
            throw new NotFoundException("Event with id=" + eventId + " is not published");
        }

        log.info("Найдено событие с id=" + eventId);

        saveHit(request);

        event.setViews(event.getViews() + 1);

        return event;
    }

    private void saveHit(HttpServletRequest request) {
        /*HitDto dto = new HitDto(
                null,
                serviceName,
                (request.getRequestURI()),
                request.getRemoteAddr(),
                LocalDateTime.now()
        );*/

        log.info("Клиент отправил эндпойнт на веб-сервис");
        //client.postHit(dto);
    }

}
