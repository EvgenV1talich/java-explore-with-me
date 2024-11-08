package ru.yandex.service.adminapi.compilation;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.dto.compilation.NewCompilationDto;
import ru.yandex.dto.compilation.UpdateCompilationRequest;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.mapper.CompilationMapper;
import ru.yandex.model.compilation.Compilation;
import ru.yandex.model.event.Event;
import ru.yandex.repository.CompilationRepository;
import ru.yandex.repository.EventRepository;

import java.text.MessageFormat;
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
    @Transactional
    public Compilation postCompilation(NewCompilationDto newCompilationDto) {

        List<Event> events = new ArrayList<>();
        Set<Long> eventsIds = newCompilationDto.getEvents();

        if (newCompilationDto.getEvents() != null) {
            events.addAll(eventRepository.findAllByIdIn(eventsIds));
        }

        Compilation compilation = compilationMapper.toCompilationFromDto(newCompilationDto, events);

        log.info(MessageFormat
                .format("Added new compilation: {0}", compilation));

        return repository.save(compilation);

    }

    @Override
    @Transactional
    public void deleteCompilation(int compId) {
        log.info(MessageFormat
                .format("Deleted Compilation id={0}", compId));

        repository.deleteById((long) compId);
    }

    @Override
    @Transactional
    public Compilation updateCompilationById(int compId, UpdateCompilationRequest updateCompilationRequest) {
        Compilation compilation = repository.findById((long) compId).orElseThrow(()
                -> new NotFoundException(MessageFormat
                .format("Compilation not found with id={0}", compId)));

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

        log.info(MessageFormat
                .format("Compilation id= {0} was updated!", compId));

        return repository.save(compilation);
    }

}
