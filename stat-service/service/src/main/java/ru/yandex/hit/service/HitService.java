package ru.yandex.hit.service;

import ru.yandex.HitDto;
import ru.yandex.StatDto;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    HitDto create(HitDto dto, String IP);

    List<StatDto> getStatByParams(LocalDateTime start,
                                  LocalDateTime end,
                                  List<String> uris,
                                  Boolean unique);

}
