package ru.yandex.controller.publicapi;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.dto.compilation.CompilationDto;
import ru.yandex.mapper.CompilationMapper;
import ru.yandex.model.compilation.Compilation;
import ru.yandex.service.publicapi.compilation.PublicCompilationService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/compilations")
public class PublicCompilationController {
    private final PublicCompilationService service;
    private final CompilationMapper mapper;

    @GetMapping
    public ResponseEntity<List<CompilationDto>> getCompilations(@RequestParam(required = false) Boolean pinned,
                                                                @RequestParam(defaultValue = "0") int from,
                                                                @RequestParam(defaultValue = "10") int size) {

        log.info("---START GET COMPILATIONS ENDPOINT---");

        return new ResponseEntity<>(pagedResponse(service.getCompilations(pinned), from, size), HttpStatus.OK);
    }

    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getCompilationById(@PathVariable int compId) {

        log.info("---START GET COMPILATION BY ID ENDPOINT---");

        return new ResponseEntity<>(mapper.toCompilationDto(service.getCompilationById(compId)), HttpStatus.OK);
    }

    private List<CompilationDto> pagedResponse(List<Compilation> compilations, int from, int size) {
        List<CompilationDto> pagedCompilations = new ArrayList<>();

        int totalCompilations = compilations.size();
        int toIndex = from + size;

        if (from <= totalCompilations) {
            if (toIndex > totalCompilations) {
                toIndex = totalCompilations;
            }
            for (Compilation compilation : compilations.subList(from, toIndex)) {
                pagedCompilations.add(mapper.toCompilationDto(compilation));
            }
            return pagedCompilations;
        } else {
            return Collections.emptyList();
        }
    }
}