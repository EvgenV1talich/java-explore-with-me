package ru.yandex.hit.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.hit.dto.HitDto;
import ru.yandex.hit.service.HitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HitController {

    private final HitService service;

    @PostMapping("/hit")
    public HitDto createHit(@RequestBody HitDto dto) {
        log.info("Received /POST request to HitController...");
        return null;
    }

    @GetMapping("/stats")
    public List<HitDto> getHitsByParams(@RequestParam LocalDateTime start,
                                        @RequestParam LocalDateTime end,
                                        @RequestParam(required = false) List<String> uris,
                                        @RequestParam(defaultValue = "false") Boolean unique) {
        log.info("Received /GET request to HitController...");
        return null;
    }

}

