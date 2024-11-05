package ru.yandex.hit.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.yandex.StatDto;
import ru.yandex.hit.model.Hit;

import java.time.LocalDateTime;
import java.util.List;

public interface HitRepository extends JpaRepository<Hit, Integer> {
    @Query(" SELECT new ru.yandex.StatDto(h.app, h.uri, COUNT(h.ip) AS hits) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND (h.uri IN (?3) OR (?3) is NULL) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(h.ip) DESC ")
    List<StatDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    @Query(" SELECT new ru.yandex.StatDto(h.app, h.uri, COUNT(DISTINCT h.ip) AS hits) " +
            "FROM Hit h " +
            "WHERE h.timestamp BETWEEN ?1 AND ?2 " +
            "AND (h.uri IN (?3)) " +
            "GROUP BY h.app, h.uri " +
            "ORDER BY COUNT(DISTINCT h.ip) DESC ")
    List<StatDto> getUniqueStats(LocalDateTime start, LocalDateTime end, List<String> uris);
}