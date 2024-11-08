package ru.yandex.hit.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.HitDto;
import ru.yandex.StatDto;
import ru.yandex.hit.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HitController {

    private final HitService service;

    @PostMapping("/hit")
    public HitDto createHit(@RequestBody HitDto dto,
                            HttpServletRequest request) {
        log.info("Received /POST request to HitController...");
        return service.create(dto, request.getRemoteAddr());
    }

    @GetMapping("/stats")
    public List<StatDto> getHitsByParams(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
                                         @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
                                         @RequestParam(required = false) List<String> uris,
                                         @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Received /GET request to HitController...");
        return service.getStatByParams(start, end, uris, unique);
    }

}

