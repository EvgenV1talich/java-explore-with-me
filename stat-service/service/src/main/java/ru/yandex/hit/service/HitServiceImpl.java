package ru.yandex.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.HitDto;
import ru.yandex.StatDto;
import ru.yandex.hit.mapper.HitMapper;
import ru.yandex.hit.repository.HitRepository;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HitServiceImpl implements HitService {

    private final HitRepository repository;
    private final HitMapper mapper;

    @Override
    public HitDto create(HitDto dto, String IP) {
        dto.setIp(IP);
        return mapper.toDto(repository.save(mapper.toHit(dto)));
    }

    @Override
    public List<StatDto> getStatByParams(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
        /*if (uris == null && !unique) {
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
            return repository.findByTimestampBetweenAndUriIn(start, end, uris)
                    .stream()
                    .map(mapper::toDto)
                    .collect(Collectors.toList());
        }*/
        if (uris == null && !unique) {
            return repository.findByTimestampBetween(start, end)
                    .stream()
                    .map(hit -> mapper.toStat(hit, repository.countDistinctByUriInIgnoreCase(uris)))
                    .sorted(Comparator.comparing(StatDto::getHits))
                    .toList();
        } else if (uris == null) {
            return repository.findDistinctByTimestampBetween(start, end)
                    .stream()
                    .map(hit -> mapper.toStat(hit, repository.countDistinctByUriInIgnoreCase(uris)))
                    .sorted(Comparator.comparing(StatDto::getHits))
                    .toList();
        } else {
            return repository.findDistinctByTimestampBetweenAndUriIn(start, end, uris)
                    .stream()
                    .map(hit -> mapper.toStat(hit, repository.countDistinctByUriInIgnoreCase(uris)))
                    .sorted(Comparator.comparing(StatDto::getHits))
                    .toList();
        }
    }
}
