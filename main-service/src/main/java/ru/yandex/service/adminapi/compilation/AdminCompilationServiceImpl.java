package ru.yandex.service.adminapi.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.dto.compilation.NewCompilationDto;
import ru.yandex.dto.compilation.UpdateCompilationRequest;
import ru.yandex.error.apierror.exceptions.ConflictException;
import ru.yandex.error.apierror.exceptions.NotFoundException;
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
            log.info("Added new compilation: " + compilation);

            return repository.save(compilation);
        } catch (ConflictException e) {
            throw new ConflictException("Something wrong when adding compilation in AdminCompilationServiceImpl postCompilation method");
        }

    }

    @Override
    public void deleteCompilation(int compId) {
        log.info("Удалена подборка с id=" + compId);

        repository.deleteById((long) compId);
    }

    @Override
    public Compilation updateCompilationById(int compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = repository.findById((long) compId).orElseThrow(()
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

        log.info("Compilation id= " + compId + " was updated!");

        return repository.save(compilation);
    }

}
