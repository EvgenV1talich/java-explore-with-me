package ru.yandex.hit.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.HitDto;
import ru.yandex.StatDto;
import ru.yandex.hit.exceptions.HitValidationException;
import ru.yandex.hit.mapper.HitMapper;
import ru.yandex.hit.model.Hit;
import ru.yandex.hit.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class HitServiceImpl implements HitService {

    private final HitRepository repository;
    private final HitMapper mapper;

    @Override
    public HitDto create(Hit hit) {
        repository.save(hit);
        return mapper.toDto(hit);
    }

    @Override
    public List<StatDto> getStatByParams(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (checkDate(start, end) || end.isBefore(start)) {
            throw new HitValidationException("Даты начала и конца указаны неверно");
        }
        if (unique) {
            return pagedResponse(repository.getUniqueStats(start, end, uris));
        } else {
            log.info("Cформирована статистика");
            return pagedResponse(repository.getStats(start, end, uris));
        }
    }

    private List<StatDto> pagedResponse(List<StatDto> views) {
        int totalViews = views.size();
        int toIndex = 20;

        if (toIndex > totalViews) {
            toIndex = totalViews;
        }
        return views.subList(0, toIndex);
    }

    private Boolean checkDate(LocalDateTime start, LocalDateTime end) {
        return !end.isBefore(LocalDateTime.now()) && !start.isBefore(LocalDateTime.now()) && !end.equals(start)
                && end.isAfter(start);
    }
}
