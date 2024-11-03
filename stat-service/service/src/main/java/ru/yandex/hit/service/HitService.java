package ru.yandex.hit.service;

import ru.yandex.hit.dto.HitDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    HitDto create(HitDto dto);

    List<HitDto> getSomeByParams(LocalDateTime start,
                                 LocalDateTime end,
                                 List<String> uris,
                                 Boolean unique);

}
