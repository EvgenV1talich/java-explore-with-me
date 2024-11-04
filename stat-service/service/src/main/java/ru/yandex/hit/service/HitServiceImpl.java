package ru.yandex.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.HitDto;
import ru.yandex.hit.mapper.HitMapper;
import ru.yandex.hit.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepository repository;
    private final HitMapper mapper;

    @Override
    public HitDto create(HitDto dto) {
        return mapper.toDto(repository.save(mapper.toHit(dto)));
    }

    @Override
    public List<HitDto> getSomeByParams(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        if (uris == null && !unique) {
            return repository.findByTimestampBetween(start, end)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
            //todo fix condition
        } else if (uris == null && unique) {
            return repository.findDistinctByTimestampBetween(start, end)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        } else {
            //TODO fix this shit
            return repository.findByTimestampBetweenAndUriContains(start, end, uris.getLast())
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }
    }
}
