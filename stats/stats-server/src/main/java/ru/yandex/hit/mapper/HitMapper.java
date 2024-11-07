package ru.yandex.hit.mapper;

import org.springframework.stereotype.Component;
import ru.yandex.HitDto;
import ru.yandex.StatDto;
import ru.yandex.hit.model.Hit;

@Component
public class HitMapper {

    public HitDto toDto(Hit hit) {
        if (hit == null) {
            return null;
        }
        HitDto dto = new HitDto();
        dto.setId(hit.getId());
        dto.setIp(hit.getIp());
        dto.setApp(hit.getApp());
        dto.setUri(hit.getUri());
        dto.setTimestamp(hit.getTimestamp());
        return dto;
    }

    public Hit toHit(HitDto dto) {
        if (dto == null) {
            return null;
        }
        Hit hit = new Hit();
        hit.setApp(dto.getApp());
        hit.setUri(dto.getUri());
        hit.setIp(dto.getIp());
        hit.setTimestamp(dto.getTimestamp());
        return hit;
    }

    public StatDto toStat(Hit hit, Long hits) {
        if (hit == null) {
            return null;
        }
        StatDto stat = new StatDto();
        stat.setApp(hit.getApp());
        stat.setUri(hit.getUri());
        stat.setHits(hits);
        return stat;
    }
}
