package ru.yandex.service.publicapi.compilation;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.error.apierror.exceptions.NotFoundException;
import ru.yandex.model.compilation.Compilation;
import ru.yandex.repository.CompilationRepository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Service
public class PublicCompilationServiceImpl implements PublicCompilationService {

    private final CompilationRepository repository;

    @Override
    public List<Compilation> getCompilations(Boolean pinned) {
        log.info("Сформирован список подборок с pinned= " + pinned);

        if (pinned != null) {
            return repository.findAllByPinned(pinned);
        } else {
            return repository.findAll();
        }
    }

    @Override
    public Compilation getCompilationById(Integer compId) {
        log.info("Поиск подборки с id=" + compId);

        return repository.findById(compId).orElseThrow(() -> new NotFoundException("Compilation not found with id = " +
                compId));
    }

}
