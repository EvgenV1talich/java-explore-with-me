package ru.yandex.mapper;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.yandex.dto.compilation.CompilationDto;
import ru.yandex.dto.compilation.NewCompilationDto;
import ru.yandex.model.compilation.Compilation;
import ru.yandex.model.event.Event;

import java.util.List;

@Component
@RequiredArgsConstructor
public class CompilationMapper {

    private final EventMapper eventMapper;

    public CompilationDto toCompilationDto(Compilation compilation) {
        if (compilation == null) {
            return null;
        }
        return new CompilationDto(
                compilation.getId(),
                compilation.getTitle(),
                compilation.getPinned(),
                eventMapper.toListEventShortDtoFromListEvents(compilation.getEvents())
        );
    }

    public Compilation toCompilationFromDto(NewCompilationDto dto, List<Event> events) {
        if (dto == null) {
            return null;
        }
        Compilation compilation = new Compilation();
        compilation.setEvents(events);
        compilation.setTitle(dto.getTitle());
        compilation.setPinned(dto.isPinned());
        return compilation;
    }

    public Compilation toCompilationFromUpdateRequest(Compilation request, List<Event> events) {
        if (request == null) {
            return null;
        }
        Compilation compilation = new Compilation();
        if (!events.isEmpty()) {
            compilation.setEvents(events);
        }

        if (request.getPinned() != null) {
            compilation.setPinned(request.getPinned());
        }

        if (request.getTitle() != null) {
            compilation.setTitle(request.getTitle());
        }
        return compilation;
    }


}
