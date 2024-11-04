package ru.yandex.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.yandex.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Integer> {
    List<Hit> findDistinctByTimestampBetween(LocalDateTime timestampStart, LocalDateTime timestampEnd);

    List<Hit> findByTimestampBetween(LocalDateTime timestampStart, LocalDateTime timestampEnd);

    List<Hit> findByTimestampBetweenAndUriIn(LocalDateTime timestampStart, LocalDateTime timestampEnd, Collection<String> uris);

    Integer countDistinctByUriInIgnoreCase(Collection<String> uris);

    List<Hit> findDistinctByTimestampBetweenAndUriIn(LocalDateTime timestampStart, LocalDateTime timestampEnd, Collection<String> uris);

}