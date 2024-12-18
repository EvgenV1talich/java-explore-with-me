package ru.yandex.controller.adminapi;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yandex.dto.compilation.CompilationDto;
import ru.yandex.dto.compilation.NewCompilationDto;
import ru.yandex.dto.compilation.UpdateCompilationRequest;
import ru.yandex.mapper.CompilationMapper;
import ru.yandex.service.adminapi.compilation.AdminCompilationService;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping(path = "/admin/compilations")
public class AdminCompilationController {

    private final AdminCompilationService service;
    private final CompilationMapper compilationMapper;

    @PostMapping
    public ResponseEntity<CompilationDto> postCompilation(@RequestBody @Valid NewCompilationDto newCompilationDto) {

        log.info("Received /POST request to AdminCompilationController:\n%s"
                .formatted(newCompilationDto.toString()));
        return new ResponseEntity<>(compilationMapper.toCompilationDto(service.postCompilation(newCompilationDto)), HttpStatus.CREATED);
    }

    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> deleteCompilation(@PathVariable int compId) {

        log.info("Received /DELETE request to AdminCompilationController: compilationId = {}", compId);
        service.deleteCompilation(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> updateCompilationById(@PathVariable int compId,
                                                                @RequestBody @Valid UpdateCompilationRequest
                                                                        updateCompilationRequest) {

        log.info("Received /PATCH request to AdminCompilationController:\n%s"
                .formatted(updateCompilationRequest.toString()));
        return new ResponseEntity<>(compilationMapper.toCompilationDto(service.updateCompilationById(compId, updateCompilationRequest)),
                HttpStatus.OK);
    }


}
