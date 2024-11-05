package ru.yandex.hit.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.yandex.HitDto;
import ru.yandex.StatDto;
import ru.yandex.hit.mapper.HitMapper;
import ru.yandex.hit.repository.HitRepository;

import java.time.LocalDateTime;
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

        if (!unique) {
            return repository.getStats(start, end, uris);
        } else {
            return repository.getUniqueStats(start, end, uris);
        }

    }
}
