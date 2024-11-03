package ru.yandex.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Integer> {
    List<Hit> findDistinctByTimestampBetween(LocalDateTime timestampStart, LocalDateTime timestampEnd);

    List<Hit> findByTimestampBetweenAndUriContains(LocalDateTime timestampStart, LocalDateTime timestampEnd, String uri);

    List<Hit> findByTimestampBetween(LocalDateTime timestampStart, LocalDateTime timestampEnd);


}