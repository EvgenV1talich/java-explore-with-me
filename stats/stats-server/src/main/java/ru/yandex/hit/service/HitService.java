package ru.yandex.hit.service;

import ru.yandex.HitDto;
import ru.yandex.StatDto;
import ru.yandex.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitService {

    HitDto create(Hit hit);

    List<StatDto> getStatByParams(LocalDateTime start,
                                  LocalDateTime end,
                                  List<String> uris,
                                  Boolean unique);

}
