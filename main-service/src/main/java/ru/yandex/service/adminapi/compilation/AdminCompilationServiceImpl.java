package ru.yandex.service.adminapi.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.dto.compilation.NewCompilationDto;
import ru.yandex.dto.compilation.UpdateCompilationRequest;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.error.apierror.exceptions.SaveException;
import ru.yandex.mapper.CompilationMapper;
import ru.yandex.model.compilation.Compilation;
import ru.yandex.model.event.Event;
import ru.yandex.repository.CompilationRepository;
import ru.yandex.repository.EventRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class AdminCompilationServiceImpl implements AdminCompilationService {

    private final CompilationRepository repository;
    private final EventRepository eventRepository;
    private final CompilationMapper compilationMapper;

    @Override
    public Compilation postCompilation(NewCompilationDto newCompilationDto) {

        List<Event> events = new ArrayList<>();
        Set<Long> eventsIds = newCompilationDto.getEvents();

        if (newCompilationDto.getEvents() != null) {
            events.addAll(eventRepository.findAllByIdIn(eventsIds));
        }

        Compilation compilation = compilationMapper.toCompilationFromDto(newCompilationDto, events);

        try {
            log.info("Добавлена новая подборка: " + compilation);

            return repository.save(compilation);
        } catch (SaveException e) {
            throw new SaveException("could not execute statement; SQL [n/a]; constraint [uq_compilation_name];"
                    + " nested exception is org.hibernate.exception.ConstraintViolationException:"
                    + " could not execute statement");
        }

    }

    @Override
    public void deleteCompilation(int compId) {
        log.info("Удалена подборка с id=" + compId);

        repository.deleteById(compId);
    }

    @Override
    public Compilation updateCompilationById(int compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = repository.findById(compId).orElseThrow(()
                -> new NotFoundException("Compilation not found with id=" + compId));

        List<Event> events = new ArrayList<>();
        Set<Long> eventsIds = updateCompilationRequest.getEvents();

        if (updateCompilationRequest.getEvents() != null) {
            events.addAll(eventRepository.findAllByIdIn(eventsIds));
        }

        if (!events.isEmpty()) {
            compilation.setEvents(events);
        }

        if (updateCompilationRequest.getPinned() != null) {
            compilation.setPinned(updateCompilationRequest.getPinned());
        }

        if (updateCompilationRequest.getTitle() != null) {
            compilation.setTitle(updateCompilationRequest.getTitle());
        }

        log.info("Обновлена с подборка id=" + compId);

        return repository.save(compilation);
    }

}
